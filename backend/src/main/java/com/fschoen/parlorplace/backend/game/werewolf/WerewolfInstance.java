package com.fschoen.parlorplace.backend.game.werewolf;

import com.fschoen.parlorplace.backend.game.management.GameInstance;
import com.fschoen.parlorplace.backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class WerewolfInstance extends GameInstance {

    private final GameService gameService;

    @Autowired
    public WerewolfInstance(GameService gameService) {
        super(gameService);
        this.gameService = gameService;
    }

}
