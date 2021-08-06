package com.fschoen.parlorplace.backend.service.implementation.game;

import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.service.AbstractGameModerator;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class WerewolfGameModerator extends AbstractGameModerator<
        WerewolfGame,
        WerewolfPlayer,
        WerewolfRuleSet,
        WerewolfGameRole,
        WerewolfGameRepository
        > {

    @Autowired
    public WerewolfGameModerator(CommunicationService communicationService, WerewolfGameRepository gameRepository) {
        super(communicationService, gameRepository);
    }

    @Override
    public void run() {

    }

}
