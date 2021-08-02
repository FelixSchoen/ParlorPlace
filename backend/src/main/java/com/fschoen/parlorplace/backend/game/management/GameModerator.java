package com.fschoen.parlorplace.backend.game.management;

import lombok.AccessLevel;
import lombok.Setter;

public abstract class GameModerator {

    @Setter(value= AccessLevel.PUBLIC)
    protected GameManager gameManager;

}
