package com.fschoen.parlorplace.backend.service.game;

import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.enumeration.VoteDrawStrategy;
import com.fschoen.parlorplace.backend.enumeration.VoteState;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.VoteException;
import com.fschoen.parlorplace.backend.game.werewolf.utility.WerewolfValueIdentifier;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.PlayerRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.repository.VoteRepository;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import com.fschoen.parlorplace.backend.utility.other.SetBuilder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractVoteService<
        V extends Vote<P, T, C, ?, D>,
        G extends Game<P, ?, ? super V, ?>,
        P extends Player<?>,
        T,
        C extends VoteCollection<T>,
        D extends Enum<D>,
        GRepo extends GameRepository<G>,
        PRepo extends PlayerRepository<P>,
        VRepo extends VoteRepository<V>
        > extends BaseGameService<G, P, GRepo> {

    protected final ConcurrentMap<Long, CompletableFuture<V>> futureMap;

    protected final PRepo playerRepository;
    protected final VRepo voteRepository;

    private final TaskExecutor taskExecutor;

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("values.werewolf-values");

    private static final long VOTE_GRACE_PERIOD_DURATION = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.VOTE_GRACE_PERIOD_DURATION));

    public AbstractVoteService(CommunicationService communicationService, UserRepository userRepository, GRepo gameRepository, PRepo playerRepository, VRepo voteRepository, TaskExecutor taskExecutor) {
        super(communicationService, userRepository, gameRepository);
        this.futureMap = new ConcurrentHashMap<>();
        this.playerRepository = playerRepository;
        this.voteRepository = voteRepository;
        this.taskExecutor = taskExecutor;
    }

    public CompletableFuture<V> requestVote(GameIdentifier gameIdentifier, VoteType voteType, VoteDrawStrategy voteDrawStrategy, Integer outcomeAmount, Map<Long, C> voteCollectionMap, D voteDescriptor, Integer round, Integer durationInSeconds) {
        log.info("Starting new Vote for Game: {}", gameIdentifier.getToken());

        G game = getActiveGame(gameIdentifier);
        V vote;

        try {
            vote = this.getVoteClass().getDeclaredConstructor().newInstance();
            vote.setGame(game);
            vote.setVoteState(VoteState.ONGOING);
            vote.setVoteType(voteType);
            vote.setVoteDrawStrategy(voteDrawStrategy);
            vote.setVoters(new SetBuilder<P>()
                    .addAll(voteCollectionMap.keySet().stream().map(id -> playerRepository.findOneById(id).orElseThrow(
                            () -> new VoteException(Messages.exception(MessageIdentifier.PLAYER_EXISTS_NOT))
                    )).collect(Collectors.toList())).build());
            vote.setVoteCollectionMap(voteCollectionMap);
            vote.setOutcome(new HashSet<>());
            vote.setOutcomeAmount(outcomeAmount);
            vote.setVoteDescriptor(voteDescriptor);
            vote.setRound(round);
            vote.setEndTime(Instant.now().plusSeconds(durationInSeconds));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.exception(MessageIdentifier.VOTE_TYPE_MISMATCH), e);
        }

        game.getVotes().add(vote);
        vote = this.voteRepository.save(vote);
        game = this.gameRepository.save(game);

        CompletableFuture<V> completableFuture = new CompletableFuture<>();
        this.futureMap.put(vote.getId(), completableFuture);

        sendGameStaleNotification(game, vote);

        VoteConcludeTask voteConcludeTask = new VoteConcludeTask(vote.getId(), (long) (durationInSeconds * 1000), true, game.getGameIdentifier());
        taskExecutor.execute(voteConcludeTask);

        return completableFuture;
    }

    public G vote(GameIdentifier gameIdentifier, long voteId, C voteCollectionProposal) {
        User principal = getPrincipal();
        log.info("User {} votes on Vote {} with Subjects: {}", principal.getUsername(), voteId, voteCollectionProposal.getSubjects());

        validateUserInSpecificActiveGame(gameIdentifier, principal);
        validateVoteExists(voteId);
        validateVoteStatusOngoing(voteId);

        G game = this.getActiveGame(gameIdentifier);
        V vote = this.voteRepository.findOneById(voteId).orElseThrow(() -> new VoteException(Messages.exception(MessageIdentifier.VOTE_EXISTS_NOT)));
        P player = game.getPlayers().stream().filter(p -> p.getUser().equals(principal)).findFirst().orElseThrow(() -> new DataConflictException(Messages.exception(MessageIdentifier.PLAYER_EXISTS_NOT)));

        validatePlayerIsVoter(voteId, player);

        // Check if vote proposal is valid
        V finalVote = vote;
        C existingVoteCollection = vote.getVoteCollectionMap().get(player.getId());
        if (existingVoteCollection == null
                || voteCollectionProposal.getSelection().size() > existingVoteCollection.getAmountVotes()
                || voteCollectionProposal.getSelection().stream().anyMatch(selection -> !finalVote.getVoteCollectionMap().get(player.getId()).getSubjects().contains(selection))
                || voteCollectionProposal.getAllowAbstain() != existingVoteCollection.getAllowAbstain()
                || !voteCollectionProposal.getAllowAbstain() && voteCollectionProposal.getAbstain() != null && voteCollectionProposal.getAbstain())
            throw new VoteException(Messages.exception(MessageIdentifier.VOTE_DATA_CONFLICT));

        // Transfer vote proposal to vote entity
        C voteCollection = vote.getVoteCollectionMap().get(player.getId());
        voteCollection.setAbstain(voteCollectionProposal.getAbstain());
        voteCollection.getSelection().removeAll(voteCollection.getSelection());
        for (T element : voteCollectionProposal.getSelection()) {
            voteCollection.getSelection().add(voteCollection.getSubjects().stream().filter(subject -> subject.equals(element)).findAny().orElseThrow(
                    () -> new VoteException(Messages.exception(MessageIdentifier.VOTE_SUBJECT_EXISTS_NOT))
            ));
        }

        vote = this.voteRepository.save(vote);

        sendGameStaleNotification(game, vote);

        // Setup VoteConcludeTask with grace period
        if (vote.getVoteCollectionMap().values().stream().allMatch(collection -> (collection.getSelection().size() == collection.getAmountVotes())
                || (collection.getAbstain() != null && collection.getAbstain()))) {
            VoteConcludeTask voteConcludeTask = new VoteConcludeTask(vote.getId(), VOTE_GRACE_PERIOD_DURATION, false, game.getGameIdentifier());
            taskExecutor.execute(voteConcludeTask);
        }

        game = this.getActiveGame(gameIdentifier);

        return game;
    }

    private List<T> getOutcome(V vote) {
        // Calculate outcome
        Map<T, Integer> votes = new HashMap<>();

        for (Entry<Long, C> entry : vote.getVoteCollectionMap().entrySet()) {
            // Add non voted-upon at the end of the list
            for (T t : entry.getValue().getSubjects()) {
                votes.putIfAbsent(t, 0);
            }

            // Add votes for all selections
            for (T t : entry.getValue().getSelection()) {
                votes.put(t, votes.get(t) + 1);
            }

        }

        // Sort by votes
        List<Entry<T, Integer>> sortedCandidates = votes.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());

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

        binList.add(currentBin);

        // Shuffle bins
        for (List<T> bin : binList) {
            Collections.shuffle(bin);
        }

        List<T> resultList = new ArrayList<>();

        while (resultList.size() < vote.getOutcomeAmount() && binList.size() > 0) {
            List<T> bin = binList.remove(0);

            int amountVotes = votes.get(bin.get(0));

            if (bin.size() > vote.getOutcomeAmount() - resultList.size() || amountVotes == 0) {
                if (vote.getVoteDrawStrategy() == VoteDrawStrategy.HARD_NO_OUTCOME)
                    return new ArrayList<>();
                if (vote.getVoteDrawStrategy() == VoteDrawStrategy.SOFT_NO_OUTCOME)
                    return resultList;
            }

            resultList.addAll(bin.stream().limit(vote.getOutcomeAmount() - resultList.size()).collect(Collectors.toList()));
        }

        return resultList;
    }

    // Abstract Methods

    protected abstract Class<V> getVoteClass();

    protected abstract Class<C> getVoteCollectionClass();

    // Utility

    protected void sendGameStaleNotification(G game, V vote) {
        sendGameStaleNotification(
                game.getGameIdentifier(),
                vote.getVoteCollectionMap().keySet().stream()
                        .map(playerId -> game.getPlayers().stream()
                                .filter(player -> player.getId().equals(playerId)).findFirst().orElseThrow(
                                        () -> new VoteException(Messages.exception(MessageIdentifier.PLAYER_EXISTS_NOT))
                                ))
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
                voteCollection.setSubjects(new SetBuilder<T>().addAll(subjects).build());
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

    protected void validatePlayerIsVoter(long voteId, P player) {
        if (this.voteRepository.findOneById(voteId).orElseThrow(
                () -> new VoteException(Messages.exception(MessageIdentifier.VOTE_EXISTS_NOT))
        ).getVoteCollectionMap().keySet().stream().noneMatch(key -> key.equals(player.getId())))
            throw new VoteException(Messages.exception(MessageIdentifier.VOTE_EXISTS_NOT));
    }

    protected void validateVoteStatusOngoing(long voteId) {
        if (!this.voteRepository.findOneById(voteId).orElseThrow(
                () -> new VoteException(Messages.exception(MessageIdentifier.VOTE_EXISTS_NOT))
        ).getVoteState().equals(VoteState.ONGOING))
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
        private final Long sleepDurationMillis;
        private final Boolean forceClose;

        private final GameIdentifier gameIdentifier;

        @SneakyThrows
        @Override
        public void run() {
            log.info("Started VoteConcludeTask for Vote {}", voteId);

            V initialVote = voteRepository.findOneById(voteId).orElseThrow(
                    () -> new VoteException(Messages.exception(MessageIdentifier.VOTE_EXISTS_NOT))
            );
            if (initialVote.getVoteState().equals(VoteState.CONCLUDED)) return;

            Thread.sleep(sleepDurationMillis);

            log.info("Trying to conclude Vote {}", voteId);

            // If already concluded do not complete future again
            V currentVote = voteRepository.findOneById(voteId).orElseThrow(
                    () -> new VoteException(Messages.exception(MessageIdentifier.VOTE_EXISTS_NOT))
            );
            if (currentVote.getVoteState().equals(VoteState.CONCLUDED)) return;

            // If changes during grace period, disregard conclusion task
            if (!forceClose && !initialVote.equals(currentVote)) return;

            // Close vote
            currentVote.setVoteState(VoteState.CONCLUDED);

            // Get outcome
            currentVote.getOutcome().addAll(getOutcome(currentVote));
            currentVote.setEndTime(Instant.now());

            voteRepository.save(currentVote);

            G currentGame = getActiveGame(gameIdentifier);

            sendGameStaleNotification(currentGame, currentVote);

            // Complete future
            if (futureMap.containsKey(currentVote.getId())) {
                futureMap.get(currentVote.getId()).complete(currentVote);
                futureMap.remove(currentVote.getId());
            }
        }

    }

}
