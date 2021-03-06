package com.fschoen.parlorplace.backend.service.game;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.GameRole;
import com.fschoen.parlorplace.backend.entity.LogEntry;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.RuleSet;
import com.fschoen.parlorplace.backend.enumeration.NotificationType;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.LogEntryRepository;
import com.fschoen.parlorplace.backend.repository.PlayerRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.utility.communication.VoiceLineClientNotification;
import com.fschoen.parlorplace.backend.utility.other.SetBuilder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
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
        PRepo extends PlayerRepository<P>,
        LRepo extends LogEntryRepository<L>
        > extends BaseGameService<G, P, GRepo> implements Runnable {

    @Setter
    protected GameIdentifier gameIdentifier;

    protected PRepo playerRepository;
    protected LRepo logEntryRepository;

    public AbstractGameModerator(CommunicationService communicationService, UserRepository userRepository, GRepo gameRepository, PRepo playerRepository, LRepo logEntryRepository) {
        super(communicationService, userRepository, gameRepository);
        this.playerRepository = playerRepository;
        this.logEntryRepository = logEntryRepository;
    }

    // Utility - Game

    protected G save(P player) {
        G game = getGame();
        if (!game.getPlayers().remove(player))
            return null;
        game.getPlayers().add(player);
        return this.gameRepository.save(game);
    }

    protected G getGame() {
        return this.getActiveGame(this.gameIdentifier);
    }

    protected Integer getCurrentRound() {
        return getGame().getRound();
    }

    protected Set<P> getAlivePlayers() {
        G game = this.getGame();
        return game.getPlayers().stream().filter(player -> player.getPlayerState() == PlayerState.ALIVE).collect(Collectors.toSet());
    }

    protected Set<P> getAllPlayersOfGame() {
        G game = getActiveGame(gameIdentifier);
        return game.getPlayers();
    }

    protected P getPlayerByPosition(int position) {
        Optional<P> playerOptional = getAllPlayersOfGame().stream().filter(player -> player.getPosition() == position).findFirst();
        return playerOptional.orElse(null);
    }

    protected Set<P> getAliveNeighbours(P player) {
        int amountPlayers = getAllPlayersOfGame().size();
        int position = player.getPosition();

        P leftNeighbour = null;
        int leftPosition = (position + 1) % amountPlayers;
        while (leftNeighbour == null) {
            P leftPlayer = getPlayerByPosition(leftPosition);
            if (leftPlayer.getPlayerState() == PlayerState.DECEASED) {
                leftPosition = (leftPosition + 1) % amountPlayers;
                continue;
            }
            leftNeighbour = leftPlayer;
        }

        P rightNeighbour = null;
        int rightPosition = (position - 1) % amountPlayers;
        while (rightNeighbour == null) {
            P rightPlayer = getPlayerByPosition(rightPosition);
            if (rightPlayer.getPlayerState() == PlayerState.DECEASED) {
                rightPosition = (rightPosition - 1) % amountPlayers;
                continue;
            }
            rightNeighbour = rightPlayer;
        }

        P finalLeftNeighbour = leftNeighbour;
        P finalRightNeighbour = rightNeighbour;

        return new SetBuilder<P>()
                .add(finalLeftNeighbour)
                .add(finalRightNeighbour).build();
    }

    protected void pause(long pauseTime) {
        try {
            Thread.sleep(pauseTime);
        } catch (InterruptedException e) {
            log.error("Interrupted while pausing game moderator.", e);
        }
    }

    // Communication

    protected void broadcastVoiceLineNotification(VoiceLineClientNotification notification) {
        this.communicationService.sendVoiceLineNotification(this.gameIdentifier, this.getAllPlayersOfGame().stream().map(Player::getUser).collect(Collectors.toSet()), notification);
    }

    protected void sendPlayerNotificationNotification(Set<P> recipients) {
        this.communicationService.sendNotification(this.gameIdentifier, recipients.stream().map(Player::getUser).collect(Collectors.toSet()), NotificationType.PLAYER_NOTIFICATION, false);
    }

}
