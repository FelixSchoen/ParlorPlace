package com.fschoen.parlorplace.backend.service;

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
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public abstract class AbstractVoteService<
        V extends Vote<P, C, D>,
        G extends Game<P, ?, V, ?>,
        P extends Player<?>,
        C extends VoteCollection<P>,
        D extends Enum<D>,
        GRepo extends GameRepository<G>,
        PRepo extends PlayerRepository<P>,
        VRepo extends VoteRepository<V>> extends BaseGameService<G, P, GRepo> {

    // TODO Not stateless, but not persistent either - I believe that this still should be ok, on a server crash we can delete open votes and restart the moderator
    protected final ConcurrentMap<Long, CompletableFuture<V>> futureMap;

    protected final PRepo playerRepository;
    protected final VRepo voteRepository;

    private final TaskExecutor taskExecutor;

    public AbstractVoteService(CommunicationService communicationService, UserRepository userRepository, GRepo gameRepository, PRepo playerRepository, VRepo voteRepository, TaskExecutor taskExecutor) {
        super(communicationService, userRepository, gameRepository);
        this.futureMap = new ConcurrentHashMap<>();
        this.playerRepository = playerRepository;
        this.voteRepository = voteRepository;
        this.taskExecutor = taskExecutor;
    }

    public CompletableFuture<V> requestVote(GameIdentifier gameIdentifier, VoteType voteType, Map<P, C> voteCollectionMap, D voteDescriptor, int durationInSeconds) {
        log.info("Starting new Vote for Game: {}", gameIdentifier.getToken());

        G game = getActiveGame(gameIdentifier);
        V vote;

        try {
            vote = this.getVoteClass().getDeclaredConstructor().newInstance();
            vote.setGame(game);
            vote.setVoteState(VoteState.ONGOING);
            vote.setVoteType(voteType);
            vote.setVoteCollectionMap(voteCollectionMap);
            vote.setVoteDescriptor(voteDescriptor);
            vote.setEndTime(LocalDateTime.now().plusSeconds(durationInSeconds));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.exception(MessageIdentifier.VOTE_TYPE_MISMATCH), e);
        }

        System.out.println(vote);

        game.getVotes().add(vote);
        vote = this.voteRepository.save(vote);
        game = this.gameRepository.save(game);

        CompletableFuture<V> completableFuture = new CompletableFuture<>();
        this.futureMap.put(vote.getId(), completableFuture);

        broadcastGameStaleNotification(game.getGameIdentifier());

        VoteConcludeTask voteConcludeTask = new VoteConcludeTask(vote.getId(), durationInSeconds, true);
        taskExecutor.execute(voteConcludeTask);

        return completableFuture;
    }

    public V vote(GameIdentifier gameIdentifier, long voteId, C voteCollection) {
        User principal = getPrincipal();
        log.info("User {} votes on Vote: {}", principal.getUsername());

        validateUserInSpecificActiveGame(gameIdentifier, principal);
        validateVoteExists(voteId);
        validateUserIsVoter(voteId, principal);
        validateVoteStatusOngoing(voteId);

        P player = this.playerRepository.findOneById(voteCollection.getPlayer().getId()).orElseThrow(() -> new DataConflictException(Messages.exception(MessageIdentifier.PLAYER_EXISTS_NOT)));
        if (!player.getUser().equals(principal))
            throw new VoteException(Messages.exception(MessageIdentifier.AUTHORIZATION_UNAUTHORIZED));

        return null;
    }

    // Abstract Methods

    protected abstract Class<V> getVoteClass();

    protected abstract Class<C> getVoteCollectionClass();

    // Validation

    protected void validateVoteExists(long voteId) {
        if (this.voteRepository.findOneById(voteId).isEmpty())
            throw new VoteException(Messages.exception(MessageIdentifier.VOTE_EXISTS_NOT));
    }

    protected void validateUserIsVoter(long voteId, User user) {
        if (this.voteRepository.findOneById(voteId).get().getVoteCollectionMap().keySet().stream().noneMatch(key -> key.getUser().equals(user)))
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
        private final Integer sleepDurationSeconds;
        private final Boolean forceClose;

        @SneakyThrows
        @Override
        public void run() {
            V initialVote = voteRepository.findOneById(voteId).orElseThrow();
            if (initialVote.getVoteState().equals(VoteState.CONCLUDED)) return;

            Thread.sleep(sleepDurationSeconds * 1000);

            V currentVote = voteRepository.findOneById(voteId).orElseThrow();
            if (currentVote.getVoteState().equals(VoteState.CONCLUDED)) return;

            if (!forceClose && !initialVote.equals(currentVote)) return;

            currentVote.setVoteState(VoteState.CONCLUDED);
            voteRepository.save(currentVote);

            futureMap.get(currentVote.getId()).complete(currentVote);
        }

    }

}
