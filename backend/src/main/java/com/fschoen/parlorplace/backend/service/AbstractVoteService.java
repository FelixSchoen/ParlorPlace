package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.Vote;
import com.fschoen.parlorplace.backend.entity.VoteCollection;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class AbstractVoteService<
        V extends Vote<P, C>,
        G extends Game<P, ?, ?>,
        P extends Player<?>,
        C extends VoteCollection<P>,
        GRepo extends GameRepository<G>> extends BaseGameService<G, P, GRepo> {


    public AbstractVoteService(CommunicationService communicationService, UserRepository userRepository, GRepo gameRepository) {
        super(communicationService, userRepository, gameRepository);
    }

    public CompletableFuture<V> requestVote(GameIdentifier gameIdentifier, VoteType voteType, Map<P, C> subjects, int durationInSeconds) {
        return null;
    }

}

/**
 * This task waits a given amount of time, after which it closes the vote and completes the given future.
 */
class VoteEndTask implements Runnable {

    @Override
    public void run() {

    }

}
