package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;

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

    protected G saveAndBroadcast(G game) {
        G savedGame = this.gameRepository.save(game);
        broadcastGameStaleNotification(game.getGameIdentifier());
        return savedGame;
    }

    // Communication

    protected void sendGameStaleNotification(GameIdentifier gameIdentifier, Set<User> recipients) {
        this.communicationService.sendGameStaleNotification(gameIdentifier, recipients);
    }

    protected void broadcastGameStaleNotification(GameIdentifier gameIdentifier) {
        G game = getActiveGame(gameIdentifier);
        Set<User> usersInGame = game.getPlayers().stream().map(Player::getUser).collect(Collectors.toSet());
        this.sendGameStaleNotification(gameIdentifier, usersInGame);
    }

    protected P getPlayerFromUser(GameIdentifier gameIdentifier, User user) {
        G game = getActiveGame(gameIdentifier);

        return game.getPlayers().stream().filter(player -> player.getUser().equals(user)).findFirst().orElseThrow();
    }

}
