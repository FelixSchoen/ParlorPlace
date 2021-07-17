package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.game.management.GameIdentifier;
import com.fschoen.parlorplace.backend.game.management.GameInstance;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.game.werewolf.WerewolfInstance;
import com.fschoen.parlorplace.backend.service.GameService;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope("prototype")
public class GameServiceImplementation implements GameService {

    private final ApplicationContext context;

    private final List<GameInstance> activesGames;

    @Autowired
    public GameServiceImplementation(ApplicationContext context) {
        this.context = context;
        this.activesGames = new ArrayList<>();
    }

    public GameIdentifier start(GameType gameType) {
        GameInstance game;

        switch (gameType) {
            case WEREWOLF -> game = context.getBean(WerewolfInstance.class);
            default -> throw new DataConflictException(Messages.getExceptionExplanationMessage("game.type.unknown"));
        }

        this.activesGames.add(game);
        return game.getGameIdentifier();
    }

    public void join(User user, GameIdentifier gameIdentifier) {

    }

    public GameIdentifier generateValidGameIdentifier() {
        int standardLength = 4;

        while (true) {
            GameIdentifier gameIdentifier = new GameIdentifier(standardLength);
            if (this.activesGames.stream().anyMatch(game -> game.getGameIdentifier().equals(gameIdentifier)))
                standardLength++;
            else
                return gameIdentifier;
        }
    }
}
