package com.fschoen.parlorplace.backend.game.management;

import com.fschoen.parlorplace.backend.entity.persistance.Game;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.entity.transience.GameState;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.management.WerewolfManager;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.PlayerRepository;
import com.fschoen.parlorplace.backend.service.GameService;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.io.NotActiveException;
import java.util.*;

public abstract class GameInstance {

    protected final GameService gameService;
    protected final GameRepository<? extends Game> gameRepository;
    protected final PlayerRepository<? extends Player> playerRepository;

    protected final WerewolfManager werewolfManager;
    @Getter(value = AccessLevel.PUBLIC)
    protected final GameIdentifier gameIdentifier;
    @Getter(value = AccessLevel.PUBLIC)
    protected Long gameId;

    public GameInstance(GameService gameService, GameRepository<? extends Game> gameRepository, PlayerRepository<? extends Player> playerRepository, WerewolfManager werewolfManager) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;

        this.werewolfManager = werewolfManager;
        this.gameIdentifier = gameService.generateValidGameIdentifier();
    }

    public abstract Game join(User user);

    public abstract Set<? extends Player> getPlayers();

    protected Game getGame() throws DataConflictException {
        Optional<? extends Game> game = this.gameRepository.findOneById(this.gameId);

        if (game.isEmpty())
            throw new DataConflictException(Messages.getExceptionExplanationMessage("game.exists.not"));

        Game foundGame = game.get();
        foundGame.setGameIdentifier(this.gameIdentifier);

        return foundGame;
    }

    protected abstract GameRepository<? extends Game> getGameRepository();

}
