package com.fschoen.parlorplace.backend.service.game;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.GameRole;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.RuleSet;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.CodeName;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.LobbyRole;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractGameService<
        G extends Game<P, RS, ?, ?>,
        P extends Player<GR>,
        RS extends RuleSet,
        GR extends GameRole,
        GRepo extends GameRepository<G>,
        M extends AbstractGameModerator<G, P, RS, GR, ?, GRepo, ?>
        > extends BaseGameService<G, P, GRepo> {

    protected final GameIdentifierService gameIdentifierService;

    private final TaskExecutor taskExecutor;
    private final ApplicationContext applicationContext;

    public AbstractGameService(
            UserRepository userRepository,
            CommunicationService communicationService,
            GameIdentifierService gameIdentifierService,
            GRepo gameRepository,
            TaskExecutor taskExecutor,
            ApplicationContext applicationContext
    ) {
        super(communicationService, userRepository, gameRepository);
        this.gameIdentifierService = gameIdentifierService;

        this.taskExecutor = taskExecutor;
        this.applicationContext = applicationContext;
    }

    /**
     * Creates a new instance of a game of {@link G} type and initializes its values.
     *
     * @return The newly created and persisted game
     */
    public G initializeGame() {
        log.info("Starting new Game: {}", this.getGameClass().getName());

        try {
            G game = this.getGameClass().getDeclaredConstructor().newInstance();
            game.setGameState(GameState.LOBBY);
            game.setPlayers(new HashSet<>());
            RS ruleSet = this.getRuleSetClass().getDeclaredConstructor().newInstance();
            game.setRuleSet(ruleSet);
            game.setRound(0);
            game.setVotes(new ArrayList<>());
            game.setLog(new ArrayList<>());
            game.setStartedAt(new Date());
            game.setGameIdentifier(this.gameIdentifierService.generateValidGameIdentifier());
            game = onInitializeGame(game);
            game = this.gameRepository.save(game);

            log.info("Created new {} instance: {}", this.getGameClass().getName(), game.getGameIdentifier().getToken());

            return game;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.exception(MessageIdentifier.GAME_TYPE_MISMATCH));
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
        validateActiveGameStateLobby(gameIdentifier);
        validateUserNotInSpecificActiveGame(gameIdentifier, user);

        G game = getActiveGame(gameIdentifier);
        P player;

        try {
            player = this.getPlayerClass().getDeclaredConstructor().newInstance();
            player.setUser(user);
            player.setGame(game);

            if (game.getPlayers().size() == 0)
                player.setLobbyRole(LobbyRole.ROLE_ADMIN);
            else
                player.setLobbyRole(LobbyRole.ROLE_USER);

            player.setPlayerState(PlayerState.ALIVE);
            player.setGameRoles(new ArrayList<>());
            player.setPosition(game.getPlayers().size());
            player.setDisconnected(false);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.exception(MessageIdentifier.GAME_TYPE_MISMATCH));
        }

        game.getPlayers().add(player);

        return saveAndBroadcast(game);
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
        validateUserInSpecificActiveGame(gameIdentifier, user);
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

            saveAndBroadcast(game);
        } else {
            player.setDisconnected(true);
            saveAndBroadcast(game);
            this.onPlayerQuit(player);
        }

    }

    public G changeGamePlayers(GameIdentifier gameIdentifier, Set<P> players) {
        User principal = getPrincipal();
        log.info("User {} changing players of Game: {}", principal.getUsername(), gameIdentifier.getToken());

        validateActiveGameExists(gameIdentifier);
        validateActiveGameStateLobby(gameIdentifier);
        validateUserLobbyAdmin(gameIdentifier, principal);

        G game = getActiveGame(gameIdentifier);
        GameException invalidRequestException = new GameException(Messages.exception(MessageIdentifier.GAME_MODIFY_INVALID));

        if (
                players.stream().min(Comparator.comparingInt(Player::getPosition)).orElseThrow(() -> invalidRequestException).getPosition() != 0
                        || players.stream().max(Comparator.comparingInt(Player::getPosition)).orElseThrow(() -> invalidRequestException).getPosition() != game.getPlayers().size() - 1
                        || players.stream().map(Player::getPosition).collect(Collectors.toSet()).size() != game.getPlayers().size()
        )
            throw new DataConflictException(Messages.exception(MessageIdentifier.GAME_MODIFY_INVALID));

        players.forEach(requestPlayer -> {
            P foundPlayer = game.getPlayers().stream().filter(existingPlayer ->
                    existingPlayer.getUser().equals(requestPlayer.getUser())).findFirst().orElseThrow(() -> invalidRequestException);
            foundPlayer.setPosition(requestPlayer.getPosition());
        });

        return saveAndBroadcast(game);
    }

    public G changeGameRuleSet(GameIdentifier gameIdentifier, RS ruleSet) {
        User principal = getPrincipal();
        log.info("User {} changing Rule Set of Game: {}", principal.getUsername(), gameIdentifier.getToken());

        validateActiveGameExists(gameIdentifier);
        validateActiveGameStateLobby(gameIdentifier);
        validateUserLobbyAdmin(gameIdentifier, principal);

        G game = getActiveGame(gameIdentifier);
        game.setRuleSet(ruleSet);

        return saveAndBroadcast(game);
    }

    /**
     * Initiates the start of the given game. Starts by assigning each player a random code name which will be used throughout
     * the game. Calls {@link AbstractGameService#onGameStart(Game)}, which allows subclasses to implement their own starting routine.
     *
     * @param gameIdentifier Game identifier of the game to start
     * @return The started game
     */
    public G startGame(GameIdentifier gameIdentifier) {
        User principal = getPrincipal();
        log.info("User {} starting Game: {}", principal.getUsername(), gameIdentifier.getToken());

        validateActiveGameExists(gameIdentifier);
        validateActiveGameStateLobby(gameIdentifier);
        validateUserLobbyAdmin(gameIdentifier, principal);

        G game = getActiveGame(gameIdentifier);

        List<CodeName> codeNames = new ArrayList<>(EnumSet.allOf(CodeName.class));
        Random random = new Random();
        for (P player : game.getPlayers()) {
            CodeName codeName = codeNames.get(random.nextInt(codeNames.size()));
            player.setCodeName(codeName);
            codeNames.remove(codeName);
        }

        game = onGameStart(game);

        game.setGameState(GameState.ONGOING);

        game = saveAndBroadcast(game);

        M moderator = applicationContext.getBean(getModeratorClass());
        moderator.setGameIdentifier(gameIdentifier);
        taskExecutor.execute(moderator);

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

    /**
     * Calls {@link AbstractGameService#getUserActiveGames(User)} with the current principal.
     *
     * @return The active games of the user
     */
    public List<G> getUserActiveGames() {
        return this.getUserActiveGames(getPrincipal());
    }

    /**
     * Returns all active games that the given user is a part of.
     *
     * @param user The user that has to be in the active game to return it
     * @return The found games
     */
    public List<G> getUserActiveGames(User user) {
        User principal = getPrincipal();
        log.info("User {} obtaining active games of User {} of type {}", principal.getUsername(), user.getUsername(), this.getGameClass().getName());

        return this.gameRepository.findAllByUserMember(user);
    }

    // Interfaces

    protected G onInitializeGame(G game) {
        return game;
    }

    /**
     * Interface to implement when a player quits.
     *
     * @param player The player that was removed from the game
     */
    protected void onPlayerQuit(P player) {
    }

    protected G onGameStart(G game) {
        return game;
    }

    // Abstract Methods

    protected abstract Class<G> getGameClass();

    protected abstract Class<P> getPlayerClass();

    protected abstract Class<RS> getRuleSetClass();

    protected abstract Class<M> getModeratorClass();

    // Validation

    protected void validateActiveGameStateLobby(GameIdentifier gameIdentifier) {
        G game = this.getActiveGame(gameIdentifier);

        if (!game.getGameState().equals(GameState.LOBBY))
            throw new GameException(Messages.exception(MessageIdentifier.GAME_STATE_STARTED));
    }

    protected void validateUserNotInSpecificActiveGame(GameIdentifier gameIdentifier, User user) throws GameException {
        G game = this.getActiveGame(gameIdentifier);

        if (game.getPlayers().stream().anyMatch(player -> player.getUser().equals(user)))
            throw new GameException(Messages.exception(MessageIdentifier.GAME_USER_INGAME_THIS));
    }

    protected void validateUserLobbyAdmin(GameIdentifier gameIdentifier, User user) throws GameException {
        validateUserInSpecificActiveGame(gameIdentifier, user);
        P player = getPlayerFromUser(gameIdentifier, user);

        if (!player.getLobbyRole().equals(LobbyRole.ROLE_ADMIN))
            throw new GameException(Messages.exception(MessageIdentifier.AUTHORIZATION_UNAUTHORIZED));
    }

}
