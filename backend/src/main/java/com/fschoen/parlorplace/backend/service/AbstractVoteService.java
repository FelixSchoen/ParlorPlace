package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.Vote;
import com.fschoen.parlorplace.backend.entity.VoteCollection;
import com.fschoen.parlorplace.backend.enumeration.VoteState;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.repository.VoteRepository;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public abstract class AbstractVoteService<
        V extends Vote<P, C>,
        G extends Game<P, ?, V, ?>,
        P extends Player<?>,
        C extends VoteCollection<P>,
        GRepo extends GameRepository<G>,
        VRepo extends VoteRepository<V>> extends BaseGameService<G, P, GRepo> {

    // TODO Not stateless, but not persistable either - I believe that this still should be ok, on a server crash we can delete open votes and restart the moderator
    private final ConcurrentMap<Long, CompletableFuture<V>> futureMap;

    private final VRepo voteRepository;

    public AbstractVoteService(CommunicationService communicationService, UserRepository userRepository, GRepo gameRepository, VRepo voteRepository) {
        super(communicationService, userRepository, gameRepository);
        this.futureMap = new ConcurrentHashMap<>();
        this.voteRepository = voteRepository;
    }

    @Transactional
    public CompletableFuture<V> requestVote(GameIdentifier gameIdentifier, VoteType voteType, Map<P, C> voteCollectionMap, int durationInSeconds) {
        G game = getActiveGame(gameIdentifier);
        V vote;

        try {
            vote = this.getVoteClass().getDeclaredConstructor().newInstance();
            vote.setGame(game);
            vote.setVoteState(VoteState.ONGOING);
            vote.setVoteType(voteType);
            vote.setVoteCollectionMap(voteCollectionMap);
            vote.setEndTime(LocalDateTime.now().plusSeconds(durationInSeconds));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.exception(MessageIdentifier.VOTE_TYPE_MISMATCH));
        }

        game.getVotes().add(vote);
        game = this.gameRepository.save(game);

        CompletableFuture<V> completableFuture = new CompletableFuture<>();
        this.futureMap.put(game.getId(), completableFuture);

        broadcastGameStaleNotification(game.getGameIdentifier());

        return completableFuture;
    }

    // Abstract Methods

    protected abstract Class<V> getVoteClass();

    protected abstract Class<C> getVoteCollectionClass();

    /**
     * Represents the task to close a vote once it is either concluded or the time has run out. If all the voters have stated
     * their preferences, this can be used to provide them with a small window of opportunity ("grace period"), in which
     * they can alter their vote before it finally closes.
     */
    private class VoteConcludeTask implements Runnable {

        private final Long voteId;
        private final Integer sleepDuration;
        private final Boolean forceClose;

        public VoteConcludeTask(Long voteId, Integer sleepDuration, Boolean forceClose) {
            this.voteId = voteId;
            this.sleepDuration = sleepDuration;
            this.forceClose = forceClose;
        }

        @SneakyThrows
        @Override
        public void run() {
            V initialVote = voteRepository.findOneById(voteId).orElseThrow();
            if (initialVote.getVoteState().equals(VoteState.CONCLUDED)) return;

            Thread.sleep(sleepDuration);

            V currentVote = voteRepository.findOneById(voteId).orElseThrow();
            if (currentVote.getVoteState().equals(VoteState.CONCLUDED)) return;

            if (!forceClose && !initialVote.equals(currentVote)) return;

            currentVote.setVoteState(VoteState.CONCLUDED);
            voteRepository.save(currentVote);

            futureMap.get(currentVote.getId()).complete(currentVote);
        }

    }

}
