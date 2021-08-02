package com.fschoen.parlorplace.backend.game.management;

import javax.annotation.PostConstruct;

public abstract class GameManager {

    protected final GameModerator gameModerator;

    public GameManager(GameModerator gameModerator) {
        this.gameModerator = gameModerator;
    }

    @PostConstruct
    public void init() {
        this.gameModerator.setGameManager(this);
    }

}
