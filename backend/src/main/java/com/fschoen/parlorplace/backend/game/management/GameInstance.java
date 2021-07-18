package com.fschoen.parlorplace.backend.game.management;

import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.entity.transience.GameState;
import com.fschoen.parlorplace.backend.game.werewolf.management.WerewolfManager;
import com.fschoen.parlorplace.backend.service.GameService;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GameInstance {

    protected final GameService gameService;
    protected final WerewolfManager werewolfManager;
    @Getter(value= AccessLevel.PUBLIC)
    protected final GameIdentifier gameIdentifier;

    @Getter(value= AccessLevel.PUBLIC)
    protected final List<Player> players;

    public GameInstance(GameService gameService, WerewolfManager werewolfManager) {
        this.gameService = gameService;
        this.werewolfManager = werewolfManager;
        this.gameIdentifier = gameService.generateValidGameIdentifier();

        this.players = Collections.synchronizedList(new ArrayList<>());
    }

    public abstract GameState join(User user);

}
