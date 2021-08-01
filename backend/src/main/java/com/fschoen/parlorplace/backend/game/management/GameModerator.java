package com.fschoen.parlorplace.backend.game.management;

import lombok.*;

public abstract class GameModerator {

    @Setter(value= AccessLevel.PUBLIC)
    protected GameManager gameManager;

}
