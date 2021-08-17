package com.fschoen.parlorplace.backend.service.game;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.BaseService;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseGameService<G extends Game<P, ?, ?, ?>, P extends Player<?>, GRepo extends GameRepository<G>> extends BaseService {

    protected final CommunicationService communicationService;

    protected final GRepo gameRepository;

    public BaseGameService(CommunicationService communicationService, UserRepository userRepository, GRepo gameRepository) {
        super(userRepository);
        this.communicationService = communicationService;
        this.gameRepository = gameRepository;
    }
    // Utility

    protected List<G> getActiveGames(GameIdentifier gameIdentifier) {
        return this.gameRepository.findAllByGameIdentifier_TokenAndEndedAt(gameIdentifier.getToken(), null);
    }

    protected G getActiveGame(GameIdentifier gameIdentifier) {
        List<G> games = this.getActiveGames(gameIdentifier);
        return games.get(0);
    }

    // Game Stale Notifications

    protected G saveAndBroadcast(G game) {
        G savedGame = this.gameRepository.save(game);
        broadcastGameStaleNotification(game.getGameIdentifier());
        return savedGame;
    }

    protected G saveAndSend(G game, Set<P> recipients) {
        G savedGame = this.gameRepository.save(game);
        this.sendGameStaleNotification(game.getGameIdentifier(), recipients);
        return savedGame;
    }

    // Communication

    protected void sendGameStaleNotification(GameIdentifier gameIdentifier, Set<P> recipients) {
        Set<User> users = recipients.stream().map(Player::getUser).collect(Collectors.toSet());
        this.communicationService.sendGameStaleNotification(gameIdentifier, users);
    }

    protected void broadcastGameStaleNotification(GameIdentifier gameIdentifier) {
        G game = getActiveGame(gameIdentifier);
        this.sendGameStaleNotification(gameIdentifier, game.getPlayers());
    }

    protected P getPlayerFromUser(GameIdentifier gameIdentifier, User user) {
        G game = getActiveGame(gameIdentifier);

        return game.getPlayers().stream().filter(player -> player.getUser().equals(user)).findFirst().orElseThrow();
    }

    // Validation

    protected void validateActiveGameExists(GameIdentifier gameIdentifier) throws GameException, DataConflictException {
        List<G> games = this.getActiveGames(gameIdentifier);

        if (games.size() == 0)
            throw new GameException(Messages.exception(MessageIdentifier.GAME_EXISTS_NOT));
        if (games.size() > 1)
            throw new DataConflictException(Messages.exception(MessageIdentifier.GAME_UNIQUE_NOT));
    }

    protected void validateUserInSpecificActiveGame(GameIdentifier gameIdentifier, User user) throws GameException {
        G game = this.getActiveGame(gameIdentifier);

        if (game.getPlayers().stream().noneMatch(player -> player.getUser().equals(user)))
            throw new GameException(Messages.exception(MessageIdentifier.GAME_USER_INGAME_NOT));
    }

}
