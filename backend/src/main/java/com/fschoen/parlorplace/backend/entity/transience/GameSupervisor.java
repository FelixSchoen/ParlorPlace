package com.fschoen.parlorplace.backend.entity.transience;

import com.fschoen.parlorplace.backend.entity.persistance.GameInstance;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameSupervisor {

    private static final GameSupervisor instance = new GameSupervisor();

    private List<GameInstance> activesGames;

    private GameSupervisor() {
        this.activesGames = new ArrayList<>();
    }

    public static GameSupervisor getInstance() {
        return instance;
    }

    public GameIdentifier generateValidGameIdentifier() {
        int standardLength = 4;

        while (true) {
            GameIdentifier gameIdentifier = new GameIdentifier(standardLength);
            if (this.activesGames.stream().anyMatch(gameInstance -> gameIdentifier.getToken().equals(gameIdentifier)))
                standardLength++;
            else
                return gameIdentifier;
        }
    }

    public void join(GameIdentifier gameIdentifier) {

    }

}
