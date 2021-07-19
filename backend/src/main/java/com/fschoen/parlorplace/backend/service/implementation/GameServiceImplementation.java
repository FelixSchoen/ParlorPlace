package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.persistance.Game;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.game.management.GameIdentifier;
import com.fschoen.parlorplace.backend.game.management.GameInstance;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.management.WerewolfInstance;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.AbstractService;
import com.fschoen.parlorplace.backend.service.GameService;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Scope("prototype")
public class GameServiceImplementation extends AbstractService implements GameService {

    private final ApplicationContext context;

    private final List<GameInstance<? extends Game, ? extends Player, ? extends GameRepository<? extends Game>>> activesGames;

    @Autowired
    public GameServiceImplementation(UserRepository userRepository, ApplicationContext context) {
        super(userRepository);
        this.context = context;
        this.activesGames = Collections.synchronizedList(new ArrayList<>());
    }

    public GameIdentifier start(GameType gameType) throws GameException {
        log.info("Starting new Game: {}", gameType);

        User principal = getPrincipal();

        if (isInGame(principal))
            throw new GameException(Messages.getExceptionExplanationMessage("game.user.ingame"));

        GameInstance<? extends Game, ? extends Player, ? extends GameRepository<? extends Game>> gameInstance;

        switch (gameType) {
            case WEREWOLF -> gameInstance = context.getBean(WerewolfInstance.class);
            default -> throw new DataConflictException(Messages.getExceptionExplanationMessage("game.type.unknown"));
        }

        this.activesGames.add(gameInstance);
        join(principal, gameInstance.getGameIdentifier());

        return gameInstance.getGameIdentifier();
    }

    public void join(User user, GameIdentifier gameIdentifier) throws DataConflictException {
        log.info("User {} joining Game: {}", user.getUsername(), gameIdentifier.getToken());

        GameInstance<? extends Game, ? extends Player, ? extends GameRepository<? extends Game>> gameInstance = getGameByGameIdentifier(gameIdentifier);
        Game game = gameInstance.join(user);
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

    private GameInstance<? extends Game, ? extends Player, ? extends GameRepository<? extends Game>> getGameByGameIdentifier(GameIdentifier gameIdentifier) throws DataConflictException {
        Optional<GameInstance<? extends Game, ? extends Player, ? extends GameRepository<? extends Game>>> gameInstanceOptional = this.activesGames.stream().filter(game -> game.getGameIdentifier().equals(gameIdentifier)).findFirst();

        if (gameInstanceOptional.isPresent())
            return gameInstanceOptional.get();

        throw new DataConflictException(Messages.getExceptionExplanationMessage("game.exists.not"));
    }

    public Boolean isInGame(User user) {
        return this.activesGames.stream().anyMatch(gameInstance -> gameInstance.getPlayers().stream().anyMatch(player -> ((Player) player).getUser().equals(user)));
    }

}
