package com.fschoen.parlorplace.backend.game.management;

import com.fschoen.parlorplace.backend.entity.persistance.Game;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.management.WerewolfManager;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.PlayerRepository;
import com.fschoen.parlorplace.backend.service.GameService;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class GameInstance<G extends Game, P extends Player, GR extends GameRepository<G>> {

    protected final GameService gameService;
    protected final GR gameRepository;

    protected final WerewolfManager werewolfManager;

    @Getter(value = AccessLevel.PUBLIC)
    protected final GameIdentifier gameIdentifier;
    @Getter(value = AccessLevel.PUBLIC)
    protected Long gameId;

    @Getter(value = AccessLevel.PUBLIC)
    protected Class<G> gameClass;
    @Getter(value = AccessLevel.PUBLIC)
    protected Class<P> playerClass;

    protected final Logger log;

    public GameInstance(Class<G> gameClass, Class<P> playerClass, GameService gameService, GR gameRepository, WerewolfManager werewolfManager, Logger log) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;

        this.werewolfManager = werewolfManager;
        this.gameIdentifier = gameService.generateValidGameIdentifier();

        this.gameClass = gameClass;
        this.playerClass = playerClass;

        this.log = log;
    }

    @PostConstruct
    public void init() {
        try {
            G game = this.gameClass.getDeclaredConstructor().newInstance();
            game.setPlayers(new HashSet<WerewolfPlayer>());
            game.setStartedAt(new Date());
            game.setGameIdentifier(this.gameIdentifier);
            game = getGameRepository().save(game);

            this.gameId = game.getId();

            log.info("Created new {} instance: {}", this.gameClass, this.gameIdentifier.getToken());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.getExceptionExplanationMessage("game.type"));
        }
    }

    public G join(User user) {
        G game = getGame();
        P player;
        try {
            player = this.playerClass.getDeclaredConstructor().newInstance();
            player.setUser(user);
            player.setGame(game);
            player.setPosition(getPlayers().size());

            game.getPlayers().add(player);
            game = getGameRepository().save(game);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.getExceptionExplanationMessage("game.type"));
        }
        return game;
    }

    public Set<P> getPlayers() {
        return this.getGame().getPlayers();
    }

    protected G getGame() throws DataConflictException {
        Optional<G> game = this.gameRepository.findOneById(this.gameId);

        if (game.isEmpty())
            throw new DataConflictException(Messages.getExceptionExplanationMessage("game.exists.not"));

        G foundGame = game.get();
        foundGame.setGameIdentifier(this.gameIdentifier);

        return foundGame;
    }

    protected GR getGameRepository() {
        return this.gameRepository;
    }

}
