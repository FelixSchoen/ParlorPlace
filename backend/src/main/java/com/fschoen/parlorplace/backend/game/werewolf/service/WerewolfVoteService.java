package com.fschoen.parlorplace.backend.game.werewolf.service;

import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayerVoteCollection;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfVote;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfVoteRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.AbstractVoteService;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WerewolfVoteService extends AbstractVoteService<WerewolfVote, WerewolfGame, WerewolfPlayer, WerewolfPlayerVoteCollection, WerewolfGameRepository, WerewolfVoteRepository> {

    @Autowired
    public WerewolfVoteService(CommunicationService communicationService, UserRepository userRepository, WerewolfGameRepository gameRepository, WerewolfVoteRepository voteRepository) {
        super(communicationService, userRepository, gameRepository, voteRepository);
    }

    @Override
    protected Class<WerewolfVote> getVoteClass() {
        return WerewolfVote.class;
    }

    @Override
    protected Class<WerewolfPlayerVoteCollection> getVoteCollectionClass() {
        return WerewolfPlayerVoteCollection.class;
    }

}
