package com.fschoen.parlorplace.backend.game.werewolf.service;

import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfVote;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfVoteCollection;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteDescriptor;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfPlayerRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfVoteRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.AbstractVoteService;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WerewolfVoteService extends AbstractVoteService<
        WerewolfVote,
        WerewolfGame,
        WerewolfPlayer,
        WerewolfPlayer,
        WerewolfVoteCollection,
        WerewolfVoteDescriptor,
        WerewolfGameRepository,
        WerewolfPlayerRepository,
        WerewolfVoteRepository
        > {

    @Autowired
    public WerewolfVoteService(CommunicationService communicationService, UserRepository userRepository, WerewolfGameRepository gameRepository, WerewolfPlayerRepository playerRepository, WerewolfVoteRepository voteRepository, @Qualifier("taskExecutor") TaskExecutor taskExecutor) {
        super(communicationService, userRepository, gameRepository, playerRepository, voteRepository, taskExecutor);
    }

    @Override
    protected Class<WerewolfVote> getVoteClass() {
        return WerewolfVote.class;
    }

    @Override
    protected Class<WerewolfVoteCollection> getVoteCollectionClass() {
        return WerewolfVoteCollection.class;
    }

}
