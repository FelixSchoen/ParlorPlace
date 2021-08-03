package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.GameRole;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.RuleSet;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.LobbyRole;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifiers;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractGameService<
        G extends Game<P, RS>,
        P extends Player<GR>,
        RS extends RuleSet,
        GR extends GameRole,
        GRepo extends GameRepository<G>
        > extends BaseService {

    protected final GameIdentifierService gameIdentifierService;

    protected final GRepo gameRepository;

    protected final Class<G> gameClass;
    protected final Class<P> playerClass;
    protected final Class<RS> ruleSetClass;

    public AbstractGameService(UserRepository userRepository, GameIdentifierService gameIdentifierService, GRepo gameRepository, Class<G> gameClass, Class<P> playerClass, Class<RS> ruleSetClass) {
        super(userRepository);
        this.gameIdentifierService = gameIdentifierService;
        this.gameRepository = gameRepository;
        this.gameClass = gameClass;
        this.playerClass = playerClass;
        this.ruleSetClass = ruleSetClass;
    }

    /**
     * Creates a new instance of a game of {@link G} type and initializes its values.
     *
     * @return The newly created and persisted game
     */
    public G initializeGame() {
        log.info("Starting new Game: {}", this.gameClass);

        try {
            G game = this.gameClass.getDeclaredConstructor().newInstance();
            game.setGameState(GameState.LOBBY);
            game.setPlayers(new HashSet<>());
            RS ruleSet = this.ruleSetClass.getDeclaredConstructor().newInstance();
            game.setRuleSet(ruleSet);
            game.setStartedAt(new Date());
            game.setGameIdentifier(this.gameIdentifierService.generateValidGameIdentifier());
            game = this.gameRepository.save(game);

            log.info("Created new {} instance: {}", this.gameClass.getName(), game.getGameIdentifier().getToken());

            return game;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.exception(MessageIdentifiers.GAME_TYPE_MISMATCH));
        }
    }

    /**
     * Calls {@link AbstractGameService#joinGame(GameIdentifier, User)} with the current principal.
     *
     * @param gameIdentifier The game identifier of the game to add the principal to
     * @return The changed game
     */
    public G joinGame(GameIdentifier gameIdentifier) {
        return this.joinGame(gameIdentifier, getPrincipal());
    }

    /**
     * Adds a user to an open active game if the user is not already part of that game.
     *
     * @param gameIdentifier The game identifier of the game to add the user to
     * @param user           The user to add to the game
     * @return The changed game
     */
    public G joinGame(GameIdentifier gameIdentifier, User user) {
        log.info("User {} joining Game: {}", user.getUsername(), gameIdentifier.getToken());

        validateActiveGameExists(gameIdentifier);
        validateActiveGameInLobby(gameIdentifier);
        validateUserNotInSpecificActiveGame(gameIdentifier, user);

        G game = getActiveGame(gameIdentifier);
        P player;

        try {
            player = this.playerClass.getDeclaredConstructor().newInstance();
            player.setUser(user);
            player.setGame(game);

            if (game.getPlayers().size() == 0)
                player.setLobbyRole(LobbyRole.ROLE_ADMIN);
            else
                player.setLobbyRole(LobbyRole.ROLE_USER);

            player.setPlayerState(PlayerState.ALIVE);
            player.setPosition(game.getPlayers().size());
            player.setDisconnected(false);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.exception(MessageIdentifiers.GAME_TYPE_MISMATCH));
        }

        game.getPlayers().add(player);

        game = this.gameRepository.save(game);
        return game;
    }

    /**
     * Calls {@link AbstractGameService#quitGame(GameIdentifier, User)} with the current principal.
     *
     * @param gameIdentifier The game identifier of the game to remove the principal from
     */
    public void quitGame(GameIdentifier gameIdentifier) {
        this.quitGame(gameIdentifier, getPrincipal());
    }

    /**
     * Tries to remove the given user from the game. Only works if the user is either the principal, or has enough authority
     * in the lobby to remove other players.
     *
     * @param gameIdentifier The game to remove the user from
     * @param user           The user to remove from the lobby
     */
    public void quitGame(GameIdentifier gameIdentifier, User user) {
        User principal = getPrincipal();
        log.info("User {} removes {} from Game: {}", principal.getUsername(), user.getUsername(), gameIdentifier.getToken());

        validateActiveGameExists(gameIdentifier);
        validateUserInActiveGame(gameIdentifier, user);
        if (!principal.equals(user))
            validateUserLobbyAdmin(gameIdentifier, principal);

        G game = getActiveGame(gameIdentifier);
        P player = game.getPlayers().stream().filter(playerCandidate -> playerCandidate.getUser().equals(user)).findFirst().orElseThrow();

        if (game.getGameState().equals(GameState.LOBBY)) {
            game.getPlayers().remove(player);

            if (game.getPlayers().size() == 0) {
                this.gameRepository.delete(game);
                return;
            }

            if (game.getPlayers().stream().noneMatch(playerCandidate -> playerCandidate.getLobbyRole().equals(LobbyRole.ROLE_ADMIN))) {
                game.getPlayers().stream().findAny().orElseThrow().setLobbyRole(LobbyRole.ROLE_ADMIN);
            }

            this.gameRepository.save(game);
        } else {
            player.setDisconnected(true);
            this.gameRepository.save(game);
            this.onPlayerQuit(player);
        }

    }

    public G changeGamePlayers(GameIdentifier gameIdentifier, Set<P> players) {
        User principal = getPrincipal();
        log.info("User {} changing players of Game: {}", principal.getUsername(), gameIdentifier.getToken());

        validateActiveGameExists(gameIdentifier);
        validateActiveGameInLobby(gameIdentifier);
        validateUserLobbyAdmin(gameIdentifier, principal);

        G game = getActiveGame(gameIdentifier);
        GameException invalidRequestException = new GameException(Messages.exception(MessageIdentifiers.GAME_MODIFY_INVALID));

        if (
                players.stream().min(Comparator.comparingInt(Player::getPosition)).orElseThrow(() -> invalidRequestException).getPosition() != 0
                        || players.stream().max(Comparator.comparingInt(Player::getPosition)).orElseThrow(() -> invalidRequestException).getPosition() != game.getPlayers().size() - 1
                        || players.stream().map(Player::getPosition).collect(Collectors.toSet()).size() != game.getPlayers().size()
        )
            throw new DataConflictException(Messages.exception(MessageIdentifiers.GAME_MODIFY_INVALID));

        G finalGame = game;
        players.forEach(requestPlayer -> {
            P foundPlayer = finalGame.getPlayers().stream().filter(existingPlayer ->
                    existingPlayer.getUser().equals(requestPlayer.getUser())).findFirst().orElseThrow(() -> invalidRequestException);
            foundPlayer.setPosition(requestPlayer.getPosition());
        });

        game = this.gameRepository.save(game);
        return game;
    }

    public G changeGameRuleSet(GameIdentifier gameIdentifier, RS ruleSet) {
        User principal = getPrincipal();
        log.info("User {} changing Rule Set of Game: {}", principal.getUsername(), gameIdentifier.getToken());

        validateActiveGameExists(gameIdentifier);
        validateUserLobbyAdmin(gameIdentifier, principal);

        G game = getActiveGame(gameIdentifier);
        game.setRuleSet(ruleSet);

        game = this.gameRepository.save(game);
        return game;
    }

    /**
     * Returns the active game.
     *
     * @param gameIdentifier The game identifier of the game to look for
     * @return The found game
     */
    public G getGame(GameIdentifier gameIdentifier) {
        validateActiveGameExists(gameIdentifier);

        return this.getActiveGame(gameIdentifier);
    }

    // Interfaces

    /**
     * Interface to implement when a player quits.
     *
     * @param player The player that was removed from the game
     */
    protected void onPlayerQuit(P player) {
    }

    // Utility

    protected List<G> getActiveGames(GameIdentifier gameIdentifier) {
        return this.gameRepository.findAllByGameIdentifier_TokenAndEndedAt(gameIdentifier.getToken(), null);
    }

    protected G getActiveGame(GameIdentifier gameIdentifier) {
        List<G> games = this.getActiveGames(gameIdentifier);
        Game<?, ?> game = games.get(0);
        if (!(this.gameClass.isInstance(game)))
            throw new DataConflictException(Messages.exception(MessageIdentifiers.GAME_TYPE_MISMATCH));
        return games.get(0);
    }

    protected P getPlayerFromUser(GameIdentifier gameIdentifier, User user) {
        validateUserInActiveGame(gameIdentifier, user);

        G game = getActiveGame(gameIdentifier);

        return game.getPlayers().stream().filter(player -> player.getUser().equals(user)).findFirst().orElseThrow();
    }

    // Validation

    protected void validateActiveGameExists(GameIdentifier gameIdentifier) throws GameException, DataConflictException {
        List<G> games = this.getActiveGames(gameIdentifier);

        if (games.size() == 0)
            throw new GameException(Messages.exception(MessageIdentifiers.GAME_EXISTS_NOT));
        if (games.size() > 1)
            throw new DataConflictException(Messages.exception(MessageIdentifiers.GAME_UNIQUE_NOT));
    }

    protected void validateActiveGameInLobby(GameIdentifier gameIdentifier) {
        G game = this.getActiveGame(gameIdentifier);

        if (!game.getGameState().equals(GameState.LOBBY))
            throw new GameException(Messages.exception(MessageIdentifiers.GAME_STATE_STARTED));
    }

    protected void validateUserInActiveGame(GameIdentifier gameIdentifier, User user) throws GameException {
        G game = this.getActiveGame(gameIdentifier);

        if (game.getPlayers().stream().noneMatch(player -> player.getUser().equals(user)))
            throw new GameException(Messages.exception(MessageIdentifiers.GAME_USER_INGAME_NOT));
    }

    protected void validateUserNotInSpecificActiveGame(GameIdentifier gameIdentifier, User user) throws GameException {
        G game = this.getActiveGame(gameIdentifier);

        if (game.getPlayers().stream().anyMatch(player -> player.getUser().equals(user)))
            throw new GameException(Messages.exception(MessageIdentifiers.GAME_USER_INGAME_THIS));
    }

    protected void validateUserLobbyAdmin(GameIdentifier gameIdentifier, User user) throws GameException {
        P player = getPlayerFromUser(gameIdentifier, user);

        if (!player.getLobbyRole().equals(LobbyRole.ROLE_ADMIN))
            throw new GameException(Messages.exception(MessageIdentifiers.AUTHORIZATION_UNAUTHORIZED));
    }

}
