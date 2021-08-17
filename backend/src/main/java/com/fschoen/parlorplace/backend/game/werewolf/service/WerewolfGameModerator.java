package com.fschoen.parlorplace.backend.game.werewolf.service;

import com.fschoen.parlorplace.backend.enumeration.CodeName;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfLogEntry;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfVote;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfLogType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoiceLineType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteDescriptor;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfLogEntryRepository;
import com.fschoen.parlorplace.backend.game.werewolf.utility.WerewolfValueIdentifier;
import com.fschoen.parlorplace.backend.game.werewolf.utility.WerewolfVoiceLineClientNotification;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.service.game.AbstractGameModerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class WerewolfGameModerator extends AbstractGameModerator<
        WerewolfGame,
        WerewolfPlayer,
        WerewolfRuleSet,
        WerewolfGameRole,
        WerewolfLogEntry,
        WerewolfGameRepository,
        WerewolfLogEntryRepository
        > {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("values.werewolf-values");

    private final int WAIT_TIME_SOCKETS_ESTABLISHED = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_SOCKETS_ESTABLISHED));
    private final int WAIT_TIME_INITIAL_ROLES = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_INITIAL_ROLES));
    private final int WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS));

    private final int VOTE_TIME_INDIVIDUAL_VOTE = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.VOTE_TIME_INDIVIDUAL_VOTE));
    private final int VOTE_TIME_GROUP_VOTE = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.VOTE_TIME_GROUP_VOTE));
    private final int VOTE_TIME_ALL_VOTE = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.VOTE_TIME_ALL_VOTE));

    private final WerewolfVoteService voteService;

    @Autowired
    public WerewolfGameModerator(WerewolfVoteService voteService, CommunicationService communicationService, UserRepository userRepository, WerewolfGameRepository gameRepository, WerewolfLogEntryRepository logEntryRepository) {
        super(communicationService, userRepository, gameRepository, logEntryRepository);
        this.voteService = voteService;
    }

    @Override
    protected Boolean isGameOngoing() {
        return true;
    }

    @SneakyThrows
    @Override
    public void run() {
        // Start game
        WerewolfGame game = getGame();
        game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.START).build());

        // Wait for all players to have established a socket connection
        pause(WAIT_TIME_SOCKETS_ESTABLISHED);

        saveAndBroadcast(game);
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.START));

        // Give all players time to read roles
        pause(WAIT_TIME_INITIAL_ROLES);

        while (this.isGameOngoing()) {
            processTransitionNight();
            processNight();
        }
    }

    private void processTransitionNight() {
        WerewolfGame game = this.getGame();
        game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.SLEEP).build());

        saveAndBroadcast(game);
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.WEREWOLVES_SLEEP));
        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);
    }

    private void processNight() throws ExecutionException, InterruptedException {
        processWerewolves();
    }

    private void processWerewolves() throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> werewolves = getAlivePlayersOfLastRoleType(WerewolfRoleType.WEREWOLF);
        Set<WerewolfPlayer> validTargets = getAlivePlayers();

        WerewolfGame game = this.getGame();
        game.getLog().add(getLogEntryTemplate(werewolves).logType(WerewolfLogType.WAKE).build());

        saveAndSend(game, werewolves);
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.WEREWOLVES_WAKE));

        CompletableFuture<WerewolfVote> werewolfVoteFuture = this.voteService.requestVote(
                this.gameIdentifier,
                VoteType.PRIVATE_PUBLIC_PUBLIC,
                1,
                this.voteService.getSameChoiceCollectionMap(
                        werewolves,
                        validTargets,
                        1,
                        false),
                WerewolfVoteDescriptor.WEREWOLVES_KILL,
                VOTE_TIME_GROUP_VOTE
                );
        werewolfVoteFuture.get();

        game.getLog().add(getLogEntryTemplate(werewolves).logType(WerewolfLogType.SLEEP).build());

        saveAndSend(game, werewolves);
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.WEREWOLVES_SLEEP));
        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);
    }

    // Utility - Game

    private Set<WerewolfPlayer> getAlivePlayers() {
        WerewolfGame game = this.getGame();
        return game.getPlayers().stream().filter(player -> player.getPlayerState() == PlayerState.ALIVE).collect(Collectors.toSet());
    }

    private Set<WerewolfPlayer> getAlivePlayersOfLastRoleType(WerewolfRoleType roleType) {
        WerewolfGame game = this.getGame();
        return getAlivePlayers().stream().filter(player -> hasLastRoleType(player, roleType)).collect(Collectors.toSet());
    }

    private boolean hasLastRoleType(WerewolfPlayer player, WerewolfRoleType roleType) {
        return player.getGameRoles().get(player.getGameRoles().size() - 1).getWerewolfRoleType() == roleType;
    }

    // Utility - Moderation

    private void pause(int pauseTime) {
        try {
            Thread.sleep(pauseTime);
        } catch (InterruptedException e) {
            log.error("Interrupted while pausing game moderator.", e);
        }
    }

    private WerewolfLogEntry.WerewolfLogEntryBuilder<?, ?> getLogEntryTemplate(Set<WerewolfPlayer> recipients) {
        return WerewolfLogEntry.builder().identifier(UUID.randomUUID()).game(getActiveGame(this.gameIdentifier)).recipients(recipients);
    }

    private WerewolfVoiceLineClientNotification getVoiceLineNotification(WerewolfVoiceLineType voiceLineType, CodeName... codeNames) {
        Set<CodeName> codeNamesSet = new HashSet<>(List.of(codeNames));
        return WerewolfVoiceLineClientNotification.builder().voiceLineType(voiceLineType).codeNames(codeNamesSet).build();
    }


}
