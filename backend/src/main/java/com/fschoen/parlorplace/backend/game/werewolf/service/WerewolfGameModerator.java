package com.fschoen.parlorplace.backend.game.werewolf.service;

import com.fschoen.parlorplace.backend.enumeration.CodeName;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.enumeration.VoteDrawStrategy;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import com.fschoen.parlorplace.backend.exception.GameEndException;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfLogEntry;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfVote;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfFaction;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@SuppressWarnings("InfiniteLoopStatement")
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

        try {
            while (true) {
                processTransitionNight();
                processNight();
                processTransitionDay();
                processDay();
            }
        } catch (GameEndException e) {
            log.info("Game {} has ended", this.gameIdentifier);
        } catch (Exception e) {
            log.error("Exception in Game {} has occurred", this.gameIdentifier, e);
        }

        // End the game and determine placements of players
        game = getGame();
        game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.END).build());
        setPlacements(game);
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.END));
        saveAndBroadcast(game);

        pause(WAIT_TIME_INITIAL_ROLES);

        game.setGameState(GameState.CONCLUDED);
        game = this.gameRepository.save(game);
        sendGameEndedNotification(gameIdentifier, game.getPlayers());

        log.info("Concluded Game {}", this.gameIdentifier);
    }

    private void processTransitionNight() {
        WerewolfGame game = this.getGame();

        // Process Village Kill
        if (game.getRound() != 0) {
            Optional<WerewolfPlayer> villagersTarget = getLastVoteOfVoteDescriptor(WerewolfVoteDescriptor.VILLAGERS_LYNCH).getOutcome().stream().findFirst();
            villagersTarget.ifPresent(this::handlePlayerDiedEvent);
        }

        game = this.getGame();

        game.setRound(game.getRound() + 1);
        game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.SLEEP).build());

        saveAndBroadcast(game);
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.VILLAGE_SLEEP));
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
                VoteDrawStrategy.CHOOSE_RANDOM,
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

    private void processTransitionDay() {
        WerewolfGame game = this.getGame();
        game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.WAKE).build());

        saveAndBroadcast(game);
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.VILLAGE_WAKE));
        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);

        // Process Werewolf Kill
        WerewolfPlayer werewolvesTarget = getLastVoteOfVoteDescriptor(WerewolfVoteDescriptor.WEREWOLVES_KILL).getOutcome().stream().findFirst().orElseThrow();
        handlePlayerDiedEvent(werewolvesTarget);
    }

    private void processDay() throws ExecutionException, InterruptedException {
        processVillagers();
    }

    private void processVillagers() throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> villagers = getAlivePlayers();
        Set<WerewolfPlayer> validTargets = getAlivePlayers();

        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.VILLAGERS_VOTE));

        for (int x = 0; x < 2; x++) {
            CompletableFuture<WerewolfVote> werewolfVoteFuture = this.voteService.requestVote(
                    this.gameIdentifier,
                    VoteType.PUBLIC_PUBLIC_PUBLIC,
                    VoteDrawStrategy.HARD_NO_OUTCOME,
                    1,
                    this.voteService.getSameChoiceCollectionMap(
                            villagers,
                            validTargets,
                            1,
                            true),
                    WerewolfVoteDescriptor.VILLAGERS_LYNCH,
                    VOTE_TIME_ALL_VOTE / (x + 1)
            );
            WerewolfVote villagerVote = werewolfVoteFuture.get();

            // Give second chance for vote
            if (villagerVote.getOutcome().size() > 0)
                break;
        }

        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);
    }

    // Utility - Misc

    private Set<WerewolfPlayer> getAlivePlayersOfLastRoleType(WerewolfRoleType roleType) {
        return getAlivePlayers().stream().filter(player -> hasLastRoleType(player, roleType)).collect(Collectors.toSet());
    }

    private Set<WerewolfPlayer> getAlivePlayersOfFaction(WerewolfFaction faction) {
        return getAlivePlayers().stream().filter(player -> getLastRole(player).getWerewolfFaction() == faction).collect(Collectors.toSet());
    }

    private WerewolfGameRole getLastRole(WerewolfPlayer player) {
        return player.getGameRoles().get(player.getGameRoles().size() - 1);
    }

    private boolean hasLastRoleType(WerewolfPlayer player, WerewolfRoleType roleType) {
        return getLastRole(player).getWerewolfRoleType() == roleType;
    }

    private WerewolfVote getLastVoteOfVoteDescriptor(WerewolfVoteDescriptor descriptor) {
        WerewolfGame game = getGame();
        return game.getVotes().stream().filter(vote -> vote.getVoteDescriptor() == descriptor).reduce((first, second) -> second).orElseThrow();
    }

    // Utility - Moderation

    private void handlePlayerDiedEvent(WerewolfPlayer target) {
        WerewolfGame game = getGame();
        WerewolfPlayer targetInDatabase = game.getPlayers().stream().filter(werewolfPlayer -> werewolfPlayer.getId().equals(target.getId())).findFirst().orElseThrow();
        targetInDatabase.setPlayerState(PlayerState.DECEASED);
        game.getLog().add(getPlayerDiedLogEntry(targetInDatabase));
        saveAndBroadcast(game);
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.DEATH));
        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);
        checkGameEnded();
    }

    private void setPlacements(WerewolfGame game) {
        Set<WerewolfPlayer> players = game.getPlayers();

        int winners = 0;

        for (WerewolfPlayer player : players) {
            if (getLastRole(player).getWerewolfFaction() == WerewolfFaction.VILLAGERS && checkVillagersWin()
                    || getLastRole(player).getWerewolfFaction() == WerewolfFaction.WEREWOLVES && checkWerewolvesWin()) {
                player.setPlacement(1);
                winners++;
            }

        }

        int finalWinners = winners;
        players.stream().filter(player -> player.getPlacement() == null).forEach(player -> player.setPlacement(finalWinners + 1));
    }

    private void checkGameEnded() {
        if (checkVillagersWin()
                || checkWerewolvesWin())
            throw new GameEndException();
    }

    private Boolean checkVillagersWin() {
        return getAlivePlayersOfLastRoleType(WerewolfRoleType.WEREWOLF).size() == 0;
    }

    private Boolean checkWerewolvesWin() {
        return getAlivePlayersOfFaction(WerewolfFaction.VILLAGERS).size() <= getAlivePlayersOfFaction(WerewolfFaction.WEREWOLVES).size();
    }

    // Utility - Communication

    private WerewolfLogEntry.WerewolfLogEntryBuilder<?, ?> getLogEntryTemplate(Set<WerewolfPlayer> recipients) {
        return WerewolfLogEntry.builder().identifier(UUID.randomUUID()).game(getActiveGame(this.gameIdentifier)).recipients(recipients);
    }

    private WerewolfLogEntry getPlayerDiedLogEntry(WerewolfPlayer werewolfPlayer) {
        return getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.DEATH).targets(new HashSet<>() {{
            add(werewolfPlayer);
        }}).build();
    }

    private WerewolfVoiceLineClientNotification getVoiceLineNotification(WerewolfVoiceLineType voiceLineType, CodeName... codeNames) {
        Set<CodeName> codeNamesSet = new HashSet<>(List.of(codeNames));
        return WerewolfVoiceLineClientNotification.builder().voiceLineType(voiceLineType).codeNames(codeNamesSet).build();
    }


}
