package com.fschoen.parlorplace.backend.game.werewolf.service;

import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayerVoteCollection;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayerWerewolfVote;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteDescriptor;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfPlayerRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfVoteRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.service.game.AbstractVoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WerewolfPlayerWerewolfVoteService extends AbstractVoteService<
        WerewolfPlayerWerewolfVote,
        WerewolfGame,
        WerewolfPlayer,
        WerewolfPlayer,
        WerewolfPlayerVoteCollection,
        WerewolfVoteDescriptor,
        WerewolfGameRepository,
        WerewolfPlayerRepository,
        WerewolfVoteRepository
        > {

    @Autowired
    public WerewolfPlayerWerewolfVoteService(CommunicationService communicationService, UserRepository userRepository, WerewolfGameRepository gameRepository, WerewolfPlayerRepository playerRepository, WerewolfVoteRepository voteRepository, @Qualifier("taskExecutor") TaskExecutor taskExecutor) {
        super(communicationService, userRepository, gameRepository, playerRepository, voteRepository, taskExecutor);
    }

    @Override
    protected Class<WerewolfPlayerWerewolfVote> getVoteClass() {
        return WerewolfPlayerWerewolfVote.class;
    }

    @Override
    protected Class<WerewolfPlayerVoteCollection> getVoteCollectionClass() {
        return WerewolfPlayerVoteCollection.class;
    }

    @Override
    protected Set<WerewolfPlayer> getGameStaleNotificationRecipients(WerewolfGame game, WerewolfPlayerWerewolfVote vote) {
        Set<WerewolfPlayer> players = super.getGameStaleNotificationRecipients(game, vote);
        players.addAll(game.getPlayers().stream().filter(player -> player.getPlayerState() == PlayerState.DECEASED).collect(Collectors.toSet()));
        return players;
    }

}
