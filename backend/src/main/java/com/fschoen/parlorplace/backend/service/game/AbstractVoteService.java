package com.fschoen.parlorplace.backend.service.game;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.entity.Vote;
import com.fschoen.parlorplace.backend.entity.VoteCollection;
import com.fschoen.parlorplace.backend.enumeration.VoteState;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.VoteException;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.PlayerRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.repository.VoteRepository;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractVoteService<
        V extends Vote<P, T, C, D>,
        G extends Game<P, ?, V, ?>,
        P extends Player<?>,
        T,
        C extends VoteCollection<T>,
        D extends Enum<D>,
        GRepo extends GameRepository<G>,
        PRepo extends PlayerRepository<P>,
        VRepo extends VoteRepository<V>> extends BaseGameService<G, P, GRepo> {

    // TODO Not stateless, but not persistent either - I believe that this still should be ok, on a server crash we can delete open votes and restart the moderator
    protected final ConcurrentMap<Long, CompletableFuture<V>> futureMap;

    protected final PRepo playerRepository;
    protected final VRepo voteRepository;

    private final TaskExecutor taskExecutor;

    private static final double GRACE_PERIOD_DURATION = 1.5;

    public AbstractVoteService(CommunicationService communicationService, UserRepository userRepository, GRepo gameRepository, PRepo playerRepository, VRepo voteRepository, TaskExecutor taskExecutor) {
        super(communicationService, userRepository, gameRepository);
        this.futureMap = new ConcurrentHashMap<>();
        this.playerRepository = playerRepository;
        this.voteRepository = voteRepository;
        this.taskExecutor = taskExecutor;
    }

    public CompletableFuture<V> requestVote(GameIdentifier gameIdentifier, VoteType voteType, Integer outcomeAmount, Map<Long, C> voteCollectionMap, D voteDescriptor, int durationInSeconds) {
        log.info("Starting new Vote for Game: {}", gameIdentifier.getToken());

        G game = getActiveGame(gameIdentifier);
        V vote;

        try {
            vote = this.getVoteClass().getDeclaredConstructor().newInstance();
            vote.setGame(game);
            vote.setVoteState(VoteState.ONGOING);
            vote.setVoteType(voteType);
            vote.setVoteCollectionMap(voteCollectionMap);
            vote.setOutcome(new HashSet<>());
            vote.setOutcomeAmount(outcomeAmount);
            vote.setVoteDescriptor(voteDescriptor);
            vote.setEndTime(Instant.now().plusSeconds(durationInSeconds));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.exception(MessageIdentifier.VOTE_TYPE_MISMATCH), e);
        }

        game.getVotes().add(vote);
        vote = this.voteRepository.save(vote);
        game = this.gameRepository.save(game);

        CompletableFuture<V> completableFuture = new CompletableFuture<>();
        this.futureMap.put(vote.getId(), completableFuture);

        broadcastGameStaleNotification(game, vote);

        VoteConcludeTask voteConcludeTask = new VoteConcludeTask(vote.getId(), (double) durationInSeconds, true, game.getGameIdentifier());
        taskExecutor.execute(voteConcludeTask);

        return completableFuture;
    }

    public G vote(GameIdentifier gameIdentifier, long voteId, C voteCollectionProposal) {
        User principal = getPrincipal();
        log.info("User {} votes on Vote {} with Subjects: {}", principal.getUsername(), voteId, voteCollectionProposal.getSubjects());

        validateUserInSpecificActiveGame(gameIdentifier, principal);
        validateVoteExists(voteId);
        validateUserIsVoter(voteId, principal);
        validateVoteStatusOngoing(voteId);

        G game = this.getActiveGame(gameIdentifier);
        V vote = this.voteRepository.findOneById(voteId).orElseThrow();

        P player = game.getPlayers().stream().filter(p -> p.getUser().equals(principal)).findFirst().orElseThrow(() -> new DataConflictException(Messages.exception(MessageIdentifier.PLAYER_EXISTS_NOT)));

        // Check if vote proposal is valid
        V finalVote = vote;
        C asdf = vote.getVoteCollectionMap().get(player);
        if (voteCollectionProposal.getSelection().size() > asdf.getAmountVotes()
                || voteCollectionProposal.getSelection().stream().anyMatch(selection -> !finalVote.getVoteCollectionMap().get(player).getSubjects().contains(selection))
                || voteCollectionProposal.getAllowAbstain() != asdf.getAllowAbstain()
                || !voteCollectionProposal.getAllowAbstain() && voteCollectionProposal.getAbstain() != null && voteCollectionProposal.getAbstain())
            throw new VoteException(Messages.exception(MessageIdentifier.VOTE_DATA_CONFLICT));

        // Transfer vote proposal to vote entity
        C voteCollection = vote.getVoteCollectionMap().get(player);
        voteCollection.setAbstain(voteCollectionProposal.getAbstain());
        voteCollection.getSelection().removeAll(voteCollection.getSelection());
        for (T element : voteCollectionProposal.getSelection()) {
            voteCollection.getSelection().add(voteCollection.getSubjects().stream().filter(subject -> subject.equals(element)).findAny().orElseThrow());
        }

        vote = this.voteRepository.save(vote);

        broadcastGameStaleNotification(game, vote);

        // Setup VoteConcludeTask with grace period
        if (vote.getVoteCollectionMap().values().stream().allMatch(collection -> collection.getSelection().size() == collection.getAmountVotes())) {
            VoteConcludeTask voteConcludeTask = new VoteConcludeTask(vote.getId(), GRACE_PERIOD_DURATION, false, game.getGameIdentifier());
            taskExecutor.execute(voteConcludeTask);
        }

        game = this.getActiveGame(gameIdentifier);

        return game;
    }

    // Abstract Methods

    protected abstract Class<V> getVoteClass();

    protected abstract Class<C> getVoteCollectionClass();

    // Utility

    protected void broadcastGameStaleNotification(G game, V vote) {
        sendGameStaleNotification(game.getGameIdentifier(), vote.getVoteCollectionMap().keySet().stream().map(
                        playerId -> game.getPlayers().stream().filter(player -> player.getId().equals(playerId)).findFirst().orElseThrow().getUser())
                .collect(Collectors.toSet()));
    }

    public Map<Long, C> getSameChoiceCollectionMap(Set<P> voters, Set<T> subjects, int amountVotes, boolean allowAbstain) {
        Map<Long, C> map = new HashMap<>();
        for (P voter : voters) {
            C voteCollection;

            try {
                voteCollection = this.getVoteCollectionClass().getDeclaredConstructor().newInstance();
                voteCollection.setAmountVotes(amountVotes);
                voteCollection.setAllowAbstain(allowAbstain);
                voteCollection.setSubjects(new HashSet<>() {{
                    addAll(subjects);
                }});
                voteCollection.setSelection(new HashSet<>());
                map.put(voter.getId(), voteCollection);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new DataConflictException(Messages.exception(MessageIdentifier.VOTE_TYPE_MISMATCH), e);
            }
        }
        return map;
    }

    // Validation

    protected void validateVoteExists(long voteId) {
        if (this.voteRepository.findOneById(voteId).isEmpty())
            throw new VoteException(Messages.exception(MessageIdentifier.VOTE_EXISTS_NOT));
    }

    protected void validateUserIsVoter(long voteId, User user) {
        if (this.voteRepository.findOneById(voteId).get().getVoteCollectionMap().keySet().stream().noneMatch(key -> key.equals(user.getId())))
            throw new VoteException(Messages.exception(MessageIdentifier.VOTE_EXISTS_NOT));
    }

    protected void validateVoteStatusOngoing(long voteId) {
        if (!this.voteRepository.findOneById(voteId).get().getVoteState().equals(VoteState.ONGOING))
            throw new VoteException(Messages.exception(MessageIdentifier.VOTE_STATUS_CONCLUDED));
    }

    // Other

    /**
     * Represents the task to close a vote once it is either concluded or the time has run out. If all the voters have stated
     * their preferences, this can be used to provide them with a small window of opportunity ("grace period"), in which
     * they can alter their vote before it finally closes.
     */
    @AllArgsConstructor
    private class VoteConcludeTask implements Runnable {

        private final Long voteId;
        private final Double sleepDurationSeconds;
        private final Boolean forceClose;

        private final GameIdentifier gameIdentifier;

        @SneakyThrows
        @Override
        public void run() {
            log.info("Started VoteConcludeTask for Vote {}", voteId);

            V initialVote = voteRepository.findOneById(voteId).orElseThrow();
            if (initialVote.getVoteState().equals(VoteState.CONCLUDED)) return;

            Thread.sleep((long) (sleepDurationSeconds * 1000));

            log.info("Trying to conclude Vote {}", voteId);

            // If already concluded do not complete future again
            V currentVote = voteRepository.findOneById(voteId).orElseThrow();
            if (currentVote.getVoteState().equals(VoteState.CONCLUDED)) return;

            // If changes during grace period, disregard conclusion task
            if (!forceClose && !initialVote.equals(currentVote)) return;

            // Close vote
            currentVote.setVoteState(VoteState.CONCLUDED);

            // Calculate outcome
            Map<T, Integer> votes = new HashMap<>();

            for (Entry<Long, C> entry : currentVote.getVoteCollectionMap().entrySet()) {
                for (T t : entry.getValue().getSelection()) {
                    votes.putIfAbsent(t, 0);
                    votes.put(t, votes.get(t) + 1);
                }
            }

            // Sort by votes
            List<Entry<T, Integer>> sortedCandidates = votes.entrySet().stream().sorted(Map.Entry.comparingByValue(Integer::compareTo)).collect(Collectors.toList());

            // Create bins of equal votes
            List<List<T>> binList = new ArrayList<>();
            List<T> currentBin = new ArrayList<>();
            int currentMaxVotes = sortedCandidates.get(0).getValue();

            for (Entry<T, Integer> entry : sortedCandidates) {
                if (entry.getValue() < currentMaxVotes) {
                    currentMaxVotes = entry.getValue();
                    binList.add(currentBin);
                    currentBin = new ArrayList<>();
                }
                currentBin.add(entry.getKey());
            }

            // Shuffle bins
            for (List<T> bin : binList) {
                Collections.shuffle(bin);
            }

            // Get top outcomeAmount choices
            List<T> flatList = binList.stream().flatMap(List::stream).collect(Collectors.toList());
            currentVote.getOutcome().addAll(flatList.stream().limit(currentVote.getOutcomeAmount()).collect(Collectors.toList()));

            voteRepository.save(currentVote);

            G currentGame = getActiveGame(gameIdentifier);

            broadcastGameStaleNotification(currentGame, currentVote);

            // Complete future
            if (futureMap.containsKey(currentVote.getId())) {
                futureMap.get(currentVote.getId()).complete(currentVote);
                futureMap.remove(currentVote.getId());
            }
        }

    }

}
