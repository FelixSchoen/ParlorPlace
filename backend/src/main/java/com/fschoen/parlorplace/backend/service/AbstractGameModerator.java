package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.GameRole;
import com.fschoen.parlorplace.backend.entity.LogEntry;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.RuleSet;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.LogEntryRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.utility.communication.VoiceLineClientNotification;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractGameModerator<
        G extends Game<P, RS, ?, ?>,
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

    // Communication

    protected void broadcastVoiceLineNotification(VoiceLineClientNotification notification) {
        this.communicationService.sendVoiceLineNotification(this.gameIdentifier, this.getAllPlayersOfGame().stream().map(Player::getUser).collect(Collectors.toSet()), notification);
    }

}
