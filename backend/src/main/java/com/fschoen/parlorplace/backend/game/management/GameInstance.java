package com.fschoen.parlorplace.backend.game.management;

import com.fschoen.parlorplace.backend.entity.persistance.*;
import com.fschoen.parlorplace.backend.enumeration.*;
import com.fschoen.parlorplace.backend.exception.*;
import com.fschoen.parlorplace.backend.game.werewolf.management.*;
import com.fschoen.parlorplace.backend.repository.*;
import com.fschoen.parlorplace.backend.service.*;
import com.fschoen.parlorplace.backend.utility.messaging.*;
import lombok.*;
import org.slf4j.*;

import javax.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

public abstract class GameInstance<G extends Game, P extends Player, GR extends GameRepository<G>, RS extends RuleSet> extends AbstractService {

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

    public GameInstance(Class<G> gameClass, Class<P> playerClass, Class<RS> ruleSetClass, GameService gameService, GR gameRepository, UserRepository userRepository, WerewolfManager werewolfManager, Logger log) {
        super(userRepository);
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
            game.setGameState(GameState.LOBBY);
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

    public G join(User user) throws GameException, DataConflictException {
        validateGameNotStarted();

        if (getPlayers().stream().anyMatch(player -> player.getUser().equals(user)))
            throw new GameException(Messages.exception("game.user.ingame.this"));

        G game = getGame();
        P player;
        try {
            player = this.playerClass.getDeclaredConstructor().newInstance();
            player.setUser(user);
            player.setDisconnected(false);
            if (getGame().getPlayers().size() == 0)
                player.setLobbyRole(LobbyRole.ROLE_ADMIN);
            else
                player.setLobbyRole(LobbyRole.ROLE_USER);
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

    /**
     * Removes a player from the game.
     *
     * @param user The user to remove from the game
     * @return {@code true} if the game was closed, {@code false} otherwise
     */
    public Boolean quit(User user) {
        User principal = getPrincipal();
        if (!user.equals(principal))
            validateUserIsLobbyAdmin(principal);

        G game = getGame();
        Set<P> players = game.getPlayers();
        P player = players.stream().filter(playerCandidate -> playerCandidate.getUser().equals(user)).findFirst().orElseThrow();

        if (!this.hasStarted) {
            players.remove(player);

            if (players.size() == 0) {
                // Destroy game
                this.gameRepository.delete(game);
                return true;
            }

            if (players.stream().noneMatch(playerCandidate -> playerCandidate.getLobbyRole().equals(LobbyRole.ROLE_ADMIN))) {
                players.stream().findAny().orElseThrow().setLobbyRole(LobbyRole.ROLE_ADMIN);
            }

            this.gameRepository.save(game);
            return false;
        } else {
            player.setDisconnected(true);
            this.gameRepository.save(game);
            return onPlayerQuit(user);
        }
    }

    public G changeLobby(Set<? extends Player> players) {
        validateGameNotStarted();
        validateUserIsLobbyAdmin(getPrincipal());

        if (!(players.stream().allMatch(playerClass::isInstance)))
            throw new DataConflictException(Messages.exception("player.type.mismatch"));

        // Change positions
        G game = getGame();

        if (players.stream().min(Comparator.comparingInt(Player::getPosition)).orElseThrow(() -> new GameException(Messages.exception("game.modify.invalid"))).getPosition() != 0
                || players.stream().max(Comparator.comparingInt(Player::getPosition)).orElseThrow(() -> new GameException(Messages.exception("game.modify.invalid"))).getPosition() != game.getPlayers().size() - 1
                || players.stream().map(Player::getPosition).collect(Collectors.toSet()).size() != game.getPlayers().size()) {
            throw new DataConflictException(Messages.exception("game.modify.invalid"));
        }

        G finalGame = game;
        players.forEach(requestPlayer -> {
            Player foundPlayer = finalGame.getPlayers().stream().filter(existingPlayer ->
                    existingPlayer.getUser().equals(requestPlayer.getUser())).findFirst().orElseThrow(() -> new GameException(Messages.exception("game.modify.invalid")));
            foundPlayer.setPosition(requestPlayer.getPosition());
        });

        game = getGameRepository().save(game);

        return game;
    }

    /**
     * Applies the rules specified in the ruleset to the game
     *
     * @param ruleSet The rule set containing the different rules of the game to apply
     * @return The modified game
     */
    public abstract G changeLobby(RuleSet ruleSet);

    public G getGame() throws DataConflictException {
        Optional<G> game = this.gameRepository.findOneById(this.gameId);

        if (game.isEmpty())
            throw new DataConflictException(Messages.exception("game.exists.not"));

        G foundGame = game.get();
        foundGame.setGameIdentifier(this.gameIdentifier);

        foundGame.toString();
        return foundGame;
    }

    // Interfaces

    /**
     * Interface to implement when a user quits.
     *
     * @param user The user that issued the quit command
     * @return True if the game was destroyed
     */
    protected Boolean onPlayerQuit(User user) {
        return false;
    };

    // Utility Getter

    public Set<P> getPlayers() {
        return this.getGame().getPlayers();
    }

    protected GR getGameRepository() {
        return this.gameRepository;
    }

    // Utility

    protected P getPlayerFromUser(User user) throws GameException {
        Optional<P> optionalPlayer = getPlayers().stream().filter(player -> player.getUser().equals(user)).findFirst();
        if (optionalPlayer.isEmpty())
            throw new GameException(Messages.exception("game.user.ingame.not"));
        return optionalPlayer.get();
    }

    protected void validateGameNotStarted() throws GameException {
        if (this.hasStarted)
            throw new GameException(Messages.exception("game.state.started"));
    }

    protected void validateUserIsLobbyAdmin(User user) throws GameException {
        P player = getPlayerFromUser(user);
        if (!player.getLobbyRole().equals(LobbyRole.ROLE_ADMIN))
            throw new GameException(Messages.exception("authorization.unauthorized"));
    }

}
