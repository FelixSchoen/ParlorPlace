package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.AbstractGameService;
import com.fschoen.parlorplace.backend.service.GameIdentifierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WerewolfGameService extends AbstractGameService<WerewolfGame, WerewolfPlayer, WerewolfRuleSet, WerewolfGameRole, WerewolfGameRepository> {

    @Autowired
    public WerewolfGameService(UserRepository userRepository, GameIdentifierService gameIdentifierService, WerewolfGameRepository gameRepository) {
        super(userRepository, gameIdentifierService, gameRepository, WerewolfGame.class, WerewolfPlayer.class, WerewolfRuleSet.class);
    }

}
