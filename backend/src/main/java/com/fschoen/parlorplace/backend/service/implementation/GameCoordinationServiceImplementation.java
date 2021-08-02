package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.RuleSet;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.exception.NotImplementedException;
import com.fschoen.parlorplace.backend.game.management.GameIdentifier;
import com.fschoen.parlorplace.backend.game.management.GameInstance;
import com.fschoen.parlorplace.backend.game.werewolf.management.WerewolfInstance;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.AbstractService;
import com.fschoen.parlorplace.backend.service.GameCoordinationService;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifiers;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class GameCoordinationServiceImplementation extends AbstractService implements GameCoordinationService {

    private final ApplicationContext context;

    private final List<GameInstance<?, ?, ?, ?>> activesGames;

    private static final Map<GameType, Class<? extends GameInstance<?, ?, ?, ?>>> GAME_INSTANCES = new HashMap<>() {{
        put(GameType.WEREWOLF, WerewolfInstance.class);
    }};

    @Autowired
    public GameCoordinationServiceImplementation(UserRepository userRepository, ApplicationContext context) {
        super(userRepository);
        this.context = context;
        this.activesGames = Collections.synchronizedList(new ArrayList<>());
    }

    public Game create(GameType gameType) throws GameException {
        log.info("Starting new Game: {}", gameType);

        User principal = getPrincipal();

        if (isInGame(principal))
            throw new GameException(Messages.exception(MessageIdentifiers.GAME_USER_INGAME));

        GameInstance<?, ?, ?, ?> gameInstance;

        if (!GAME_INSTANCES.containsKey(gameType))
            throw new NotImplementedException(Messages.exception(MessageIdentifiers.GAME_TYPE_UNKNOWN));

        gameInstance = context.getBean(GAME_INSTANCES.get(gameType));

        this.activesGames.add(gameInstance);
        Game game = join(gameInstance.getGameIdentifier());

        return game;
    }

    public Game join(GameIdentifier gameIdentifier) throws GameException, DataConflictException {
        User principal = getPrincipal();
        log.info("User {} joining Game: {}", principal.getUsername(), gameIdentifier.getToken());

        if (isInGame(principal))
            throw new GameException(Messages.exception(MessageIdentifiers.GAME_USER_INGAME));

        GameInstance<?, ?, ?, ?> gameInstance = getGameByGameIdentifier(gameIdentifier);
        return gameInstance.join(principal);
    }

    public void quit(GameIdentifier gameIdentifier, User user) throws GameException {
        User principal = getPrincipal();
        if (user == null) user = principal;
        log.info("User {} removes {} from Game: {}", principal.getUsername(), user.getUsername(), gameIdentifier.getToken());

        if (!isInGame(user, gameIdentifier))
            throw new GameException(Messages.exception(MessageIdentifiers.GAME_USER_INGAME_NOT));

        GameInstance<?, ?, ?, ?> gameInstance = getGameByGameIdentifier(gameIdentifier);

        if (gameInstance.quit(user)) {
            this.activesGames.remove(gameInstance);
        }
    }

    public Game changeLobby(GameIdentifier gameIdentifier, Set<? extends Player> players) throws GameException {
        User principal = getPrincipal();
        log.info("User {} changing Lobby of Game: {}", principal.getUsername(), gameIdentifier.getToken());

        if (isNotInGame(principal, gameIdentifier))
            throw new GameException(Messages.exception(MessageIdentifiers.GAME_USER_INGAME_NOT));

        GameInstance<?, ?, ?, ?> gameInstance = getGameByGameIdentifier(gameIdentifier);

        return gameInstance.changeLobby(players);
    }

    public Game changeLobby(GameIdentifier gameIdentifier, RuleSet ruleSet) {
        User principal = getPrincipal();
        log.info("User {} changing Rule Set of Game: {}", principal.getUsername(), gameIdentifier.getToken());

        if (isNotInGame(principal, gameIdentifier))
            throw new GameException(Messages.exception(MessageIdentifiers.GAME_USER_INGAME_NOT));

        GameInstance<?, ?, ?, ?> gameInstance = getGameByGameIdentifier(gameIdentifier);

        return gameInstance.changeLobby(ruleSet);
    }

    public Game getGameState(GameIdentifier gameIdentifier) {
        User principal = getPrincipal();
        log.info("User {} obtaining information about Game: {}", principal.getUsername(), gameIdentifier.getToken());

        if (isNotInGame(principal, gameIdentifier))
            throw new GameException(Messages.exception(MessageIdentifiers.GAME_USER_INGAME_NOT));

        GameInstance<?, ?, ?, ?> gameInstance = getGameByGameIdentifier(gameIdentifier);

        return gameInstance.getGame();
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

    private GameInstance<?, ?, ?, ?> getGameByGameIdentifier(GameIdentifier gameIdentifier) throws DataConflictException {
        Optional<GameInstance<?, ?, ?, ?>> gameInstanceOptional = this.activesGames.stream().filter(game -> game.getGameIdentifier().equals(gameIdentifier)).findFirst();

        if (gameInstanceOptional.isPresent())
            return gameInstanceOptional.get();

        throw new DataConflictException(Messages.exception(MessageIdentifiers.GAME_EXISTS_NOT));
    }

    public Boolean isInGame(User user) {
        return this.activesGames.stream().anyMatch(gameInstance -> gameInstance.getPlayers().stream().anyMatch(player -> player.getUser().equals(user)));
    }

    public Boolean isInGame(User user, GameIdentifier gameIdentifier) {
        return this.activesGames.stream().anyMatch(gameInstance -> gameInstance.getGameIdentifier().equals(gameIdentifier)
                && gameInstance.getPlayers().stream().anyMatch(player -> player.getUser().equals(user)));
    }

    public Boolean isNotInGame(User user, GameIdentifier gameIdentifier) {
        GameInstance<?, ?, ?, ?> gameInstance = getGameByGameIdentifier(gameIdentifier);
        return gameInstance.getPlayers().stream().noneMatch(player -> player.getUser().equals(user));
    }

}
