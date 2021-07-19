package com.fschoen.parlorplace.backend.game.werewolf.management;

import com.fschoen.parlorplace.backend.game.management.GameInstance;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfPlayerRepository;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.PlayerRepository;
import com.fschoen.parlorplace.backend.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashSet;

@Component
@Scope("prototype")
@Slf4j
public class WerewolfInstance extends GameInstance<WerewolfGame, WerewolfPlayer, WerewolfGameRepository> {

    @Autowired
    public WerewolfInstance(GameService gameService, WerewolfGameRepository gameRepository, WerewolfManager werewolfManager) {
        super(WerewolfGame.class, WerewolfPlayer.class, gameService, gameRepository, werewolfManager, log);
    }

}
