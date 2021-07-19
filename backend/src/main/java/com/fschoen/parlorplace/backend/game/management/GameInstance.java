package com.fschoen.parlorplace.backend.game.management;

import com.fschoen.parlorplace.backend.entity.persistance.Game;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.entity.persistance.RuleSet;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.game.werewolf.management.WerewolfManager;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.service.GameService;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class GameInstance<G extends Game, P extends Player, GR extends GameRepository<G>, RS extends RuleSet> {

    protected final GameService gameService;
    protected final GR gameRepository;

    protected final WerewolfManager werewolfManager;

    @Getter(value = AccessLevel.PUBLIC)
    protected final GameIdentifier gameIdentifier;
    @Getter(value = AccessLevel.PUBLIC)
    protected Long gameId;
    
    protected Class<G> gameClass;
    protected Class<P> playerClass;
    protected Class<RS> ruleSetClass;

    protected Boolean hasStarted;

    protected final Logger log;

    public GameInstance(Class<G> gameClass, Class<P> playerClass, Class<RS> ruleSetClass, GameService gameService, GR gameRepository, WerewolfManager werewolfManager, Logger log) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;

        this.werewolfManager = werewolfManager;
        this.gameIdentifier = gameService.generateValidGameIdentifier();

        this.gameClass = gameClass;
        this.playerClass = playerClass;
        this.ruleSetClass = ruleSetClass;

        this.hasStarted = false;

        this.log = log;
    }

    @PostConstruct
    public void init() {
        try {
            G game = this.gameClass.getDeclaredConstructor().newInstance();
            game.setPlayers(new HashSet<P>());
            RS ruleSet = this.ruleSetClass.getDeclaredConstructor().newInstance();
            game.setRuleSet(ruleSet);
            game.setStartedAt(new Date());
            game.setGameIdentifier(this.gameIdentifier);
            game = getGameRepository().save(game);

            this.gameId = game.getId();

            log.info("Created new {} instance: {}", this.gameClass, this.gameIdentifier.getToken());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.exception("game.type.mismatch"));
        }
    }

    public G join(User user) {
        validateGameNotStarted();

        if (getPlayers().stream().anyMatch(player -> player.getUser().equals(user)))
            throw new GameException(Messages.exception("game.user.ingame.this"));

        G game = getGame();
        P player;
        try {
            player = this.playerClass.getDeclaredConstructor().newInstance();
            player.setUser(user);
            player.setPlayerState(PlayerState.ALIVE);
            player.setGame(game);
            player.setPosition(getPlayers().size());

            game.getPlayers().add(player);
            game = getGameRepository().save(game);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.exception("game.type.mismatch"));
        }
        return game;
    }

    public G changeLobby(Set<Player> players) {
        if (! (players.stream().allMatch(playerClass::isInstance)))
            throw new DataConflictException(Messages.exception("player.type.mismatch"));

        return null;
    }

    // Utility Getter

    public Set<P> getPlayers() {
        return this.getGame().getPlayers();
    }

    protected G getGame() throws DataConflictException {
        Optional<G> game = this.gameRepository.findOneById(this.gameId);

        if (game.isEmpty())
            throw new DataConflictException(Messages.exception("game.exists.not"));

        G foundGame = game.get();
        foundGame.setGameIdentifier(this.gameIdentifier);

        return foundGame;
    }

    protected GR getGameRepository() {
        return this.gameRepository;
    }

    // Utility

    protected void validateGameNotStarted() {
        if (this.hasStarted)
            throw new GameException(Messages.exception("game.state.started"));
    }

}
