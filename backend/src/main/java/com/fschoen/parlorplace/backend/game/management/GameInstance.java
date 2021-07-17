package com.fschoen.parlorplace.backend.game.management;

import com.fschoen.parlorplace.backend.service.GameService;
import lombok.Data;

@Data
public abstract class GameInstance {

    protected GameIdentifier gameIdentifier;

    public GameInstance(GameService gameService) {
        this.gameIdentifier = gameService.generateValidGameIdentifier();
    }

}
