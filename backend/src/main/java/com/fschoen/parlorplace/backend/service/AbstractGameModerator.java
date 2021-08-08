package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.GameRole;
import com.fschoen.parlorplace.backend.entity.LogEntry;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.RuleSet;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.LogEntryRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractGameModerator<
        G extends Game<P, RS, ?>,
        P extends Player<GR>,
        RS extends RuleSet,
        GR extends GameRole,
        L extends LogEntry<P>,
        GRepo extends GameRepository<G>,
        LRepo extends LogEntryRepository<L>
        > extends BaseGameService<G, P, GRepo> implements Runnable {

    @Setter
    protected GameIdentifier gameIdentifier;

    protected LRepo logEntryRepository;

    public AbstractGameModerator(CommunicationService communicationService, UserRepository userRepository, GRepo gameRepository, LRepo logEntryRepository) {
        super(communicationService, userRepository, gameRepository);
        this.logEntryRepository = logEntryRepository;
    }

    protected abstract Boolean isGameOngoing();

    // Utility

    protected G getGame() {
        return this.getActiveGame(this.gameIdentifier);
    }

    protected Set<P> getAllPlayersOfGame() {
        G game = getActiveGame(gameIdentifier);
        return game.getPlayers();
    }

    protected L saveAndBroadcast(L log) {
        L savedLogEntry = this.logEntryRepository.save(log);
        this.broadcastLogsStaleNotification(this.gameIdentifier);
        return savedLogEntry;
    }

    // Communication

    protected void sendLogsStaleNotification(GameIdentifier gameIdentifier, Set<User> recipients) {
        this.communicationService.sendLogsStaleNotification(gameIdentifier, recipients);
    }

    protected void broadcastLogsStaleNotification(GameIdentifier gameIdentifier) {
        G game = getActiveGame(gameIdentifier);
        Set<User> usersInGame = game.getPlayers().stream().map(Player::getUser).collect(Collectors.toSet());
        this.sendLogsStaleNotification(gameIdentifier, usersInGame);
    }

}
