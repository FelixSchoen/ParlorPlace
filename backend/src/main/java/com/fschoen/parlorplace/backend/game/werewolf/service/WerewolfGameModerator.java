package com.fschoen.parlorplace.backend.game.werewolf.service;

import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.enumeration.CodeName;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.enumeration.VoteDrawStrategy;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import com.fschoen.parlorplace.backend.exception.GameEndException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfLogEntry;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfVote;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.WitchWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfFaction;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfLogType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoiceLineType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteDescriptor;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfLogEntryRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfPlayerRepository;
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

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
        WerewolfPlayerRepository,
        WerewolfLogEntryRepository
        > {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("values.werewolf-values");

    private final int WAIT_TIME_SOCKETS_ESTABLISHED = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_SOCKETS_ESTABLISHED));
    private final int WAIT_TIME_INITIAL_ROLES = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_INITIAL_ROLES));
    private final int WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS));
    private final int WAIT_TIME_NEW_INFORMATION = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_NEW_INFORMATION));

    private final int VOTE_TIME_INDIVIDUAL_VOTE = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.VOTE_TIME_INDIVIDUAL_VOTE));
    private final int VOTE_TIME_GROUP_VOTE = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.VOTE_TIME_GROUP_VOTE));
    private final int VOTE_TIME_ALL_VOTE = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.VOTE_TIME_ALL_VOTE));

    private final WerewolfVoteService voteService;

    @Autowired
    public WerewolfGameModerator(WerewolfVoteService voteService, CommunicationService communicationService, UserRepository userRepository, WerewolfGameRepository gameRepository, WerewolfPlayerRepository playerRepository, WerewolfLogEntryRepository logEntryRepository) {
        super(communicationService, userRepository, gameRepository, playerRepository, logEntryRepository);
        this.voteService = voteService;
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
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.END));
        saveAndBroadcast(game);

        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);

        setPlacements(game);
        game.setGameState(GameState.CONCLUDED);
        game.setEndedAt(Instant.now());
        game = this.gameRepository.save(game);
        sendGameEndedNotification(gameIdentifier, game.getPlayers());

        log.info("Concluded Game {}", this.gameIdentifier);
    }

    private void processTransitionNight() {
        WerewolfGame game = this.getGame();

        // Process Village Kill
        if (game.getRound() != 0) {
            for (WerewolfVote vote : getVotesInRoundOfVoteDescriptor(getCurrentRound(), WerewolfVoteDescriptor.VILLAGERS_LYNCH)) {
                for (WerewolfPlayer target : vote.getOutcome()) {
                    handlePlayerDiedEvent(target);
                }
            }
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
        processAllWitches();
        processAllSeers();
    }

    private void processWerewolves() throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> werewolves = getAlivePlayersOfLastRoleType(WerewolfRoleType.WEREWOLF);
        Set<WerewolfPlayer> validTargets = getAlivePlayers();

        processNightPreVote(werewolves, WerewolfVoiceLineType.WEREWOLVES_WAKE);

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
                getCurrentRound(),
                VOTE_TIME_GROUP_VOTE
        );
        werewolfVoteFuture.get();

        processNightPostVote(werewolves, WerewolfVoiceLineType.WEREWOLVES_SLEEP);
    }

    private void processAllWitches() throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> witches = getAlivePlayersOfLastRoleType(WerewolfRoleType.WITCH);
        for (WerewolfPlayer witch : witches)
            processWitch(witch);
    }

    private void processWitch(WerewolfPlayer witch) throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> witchSet = new HashSet<>() {{
            add(witch);
        }};

        processNightPreVote(witchSet, WerewolfVoiceLineType.WITCH_WAKE, witch);

        // --- Logic Start ---

        Instant startTime = Instant.now();

        WitchWerewolfGameRole witchWerewolfGameRole = (WitchWerewolfGameRole) getLastRole(witch);

        if (!witchWerewolfGameRole.getHasHealed()) {
            Set<WerewolfPlayer> validHealTargets = new HashSet<>();
            getVotesInRoundOfVoteDescriptor(getCurrentRound(), WerewolfVoteDescriptor.WEREWOLVES_KILL).forEach(vote -> validHealTargets.addAll(vote.getOutcome()));

            CompletableFuture<WerewolfVote> witchHealVoteFuture = this.voteService.requestVote(
                    this.gameIdentifier,
                    VoteType.PRIVATE_PUBLIC_PUBLIC,
                    VoteDrawStrategy.HARD_NO_OUTCOME,
                    1,
                    this.voteService.getSameChoiceCollectionMap(
                            witchSet,
                            validHealTargets,
                            1,
                            true),
                    WerewolfVoteDescriptor.WITCH_HEAL,
                    getCurrentRound(),
                    VOTE_TIME_INDIVIDUAL_VOTE
            );
            WerewolfVote witchHealVote = witchHealVoteFuture.get();

            if (witchHealVote.getOutcome().size() > 0) {
                WerewolfPlayer witchHealTarget = witchHealVote.getOutcome().stream().findAny().orElseThrow();
                witchWerewolfGameRole.setHasHealed(true);
                WerewolfGame game = save(witch);
                if (game == null) {
                    throw new GameException("Could not save witch");
                }

                game.getLog().add(getLogEntryTemplate(witchSet).logType(WerewolfLogType.WITCH_HEAL).targets(new HashSet<>() {{
                    add(witchHealTarget);
                }}).build());
                saveAndSend(game, witchSet);
            }
        }

        if (!witchWerewolfGameRole.getHasKilled()) {
            Set<WerewolfPlayer> validKillTargets = getAlivePlayers();

            CompletableFuture<WerewolfVote> witchHealVoteFuture = this.voteService.requestVote(
                    this.gameIdentifier,
                    VoteType.PRIVATE_PUBLIC_PUBLIC,
                    VoteDrawStrategy.HARD_NO_OUTCOME,
                    1,
                    this.voteService.getSameChoiceCollectionMap(
                            witchSet,
                            validKillTargets,
                            1,
                            true),
                    WerewolfVoteDescriptor.WITCH_KILL,
                    getCurrentRound(),
                    VOTE_TIME_INDIVIDUAL_VOTE
            );
            WerewolfVote witchHealVote = witchHealVoteFuture.get();

            if (witchHealVote.getOutcome().size() > 0) {
                WerewolfPlayer witchKillTarget = witchHealVote.getOutcome().stream().findAny().orElseThrow();
                witchWerewolfGameRole.setHasKilled(true);
                WerewolfGame game = save(witch);

                game.getLog().add(getLogEntryTemplate(witchSet).logType(WerewolfLogType.WITCH_KILL).targets(new HashSet<>() {{
                    add(witchKillTarget);
                }}).build());
                saveAndSend(game, witchSet);
            }
        }

        // --- Logic End ---

        processNightPostVote(witchSet, WerewolfVoiceLineType.PLAYER_SLEEP, witch);
    }

    private void processAllSeers() throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> seers = getAlivePlayersOfLastRoleType(WerewolfRoleType.SEER);
        for (WerewolfPlayer seer : seers)
            processSeer(seer);
    }

    private void processSeer(WerewolfPlayer seer) throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> seerSet = new HashSet<>() {{
            add(seer);
        }};
        Set<WerewolfPlayer> validTargets = getAlivePlayers();
        validTargets.remove(seer);

        processNightPreVote(seerSet, WerewolfVoiceLineType.SEER_WAKE, seer);

        CompletableFuture<WerewolfVote> seerVoteFuture = this.voteService.requestVote(
                this.gameIdentifier,
                VoteType.PRIVATE_PUBLIC_PUBLIC,
                VoteDrawStrategy.CHOOSE_RANDOM,
                1,
                this.voteService.getSameChoiceCollectionMap(
                        seerSet,
                        validTargets,
                        1,
                        false),
                WerewolfVoteDescriptor.SEER_SEE,
                getCurrentRound(),
                VOTE_TIME_INDIVIDUAL_VOTE
        );
        WerewolfVote seerVote = seerVoteFuture.get();

        WerewolfPlayer seerTarget = seerVote.getOutcome().stream().findFirst().orElseThrow();

        // --- Logic Start ---

        WerewolfGame game = this.getGame();
        if (hasLastRoleType(seerTarget, WerewolfRoleType.WEREWOLF)) {
            game.getLog().add(getLogEntryTemplate(seerSet).logType(WerewolfLogType.SEER_SUCCESS).targets(new HashSet<>() {{
                add(seerTarget);
            }}).build());
        } else {
            game.getLog().add(getLogEntryTemplate(seerSet).logType(WerewolfLogType.SEER_FAILURE).targets(new HashSet<>() {{
                add(seerTarget);
            }}).build());
        }
        saveAndSend(game, seerSet);
        pause(WAIT_TIME_NEW_INFORMATION);

        // --- Logic End ---

        processNightPostVote(seerSet, WerewolfVoiceLineType.PLAYER_SLEEP, seer);
    }

    private void processTransitionDay() {
        WerewolfGame game = this.getGame();
        game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.WAKE).build());

        saveAndBroadcast(game);
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.VILLAGE_WAKE));
        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);

        // Process Werewolf Kill
        for (WerewolfVote werewolfVote : getVotesInRoundOfVoteDescriptor(getCurrentRound(), WerewolfVoteDescriptor.WEREWOLVES_KILL)) {
            for (WerewolfPlayer target : werewolfVote.getOutcome()) {
                Set<WerewolfPlayer> witchHealTargets = new HashSet<>();
                getVotesInRoundOfVoteDescriptor(getCurrentRound(), WerewolfVoteDescriptor.WITCH_HEAL).forEach(witchHealVote -> witchHealTargets.addAll(witchHealVote.getOutcome()));

                if (!witchHealTargets.contains(target))
                    handlePlayerDiedEvent(target);
            }
        }
    }

    private void processDay() throws ExecutionException, InterruptedException {
        processVillagers();
    }

    private void processVillagers() throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> villagers = getAlivePlayers();
        Set<WerewolfPlayer> validTargets = getAlivePlayers();

        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.VILLAGE_VOTE));

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
                    getCurrentRound(),
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

    private Set<WerewolfVote> getVotesInRoundOfVoteDescriptor(Integer round, WerewolfVoteDescriptor descriptor) {
        WerewolfGame game = getGame();
        return game.getVotes().stream().filter(vote -> (vote.getVoteDescriptor() == descriptor && Objects.equals(vote.getRound(), round))).collect(Collectors.toSet());
    }

    // Utility - Moderation

    private void processNightPreVote(Set<WerewolfPlayer> playerSet, WerewolfVoiceLineType voiceLineType, WerewolfPlayer... players) {
        WerewolfGame game = this.getGame();
        game.getLog().add(getLogEntryTemplate(playerSet).logType(WerewolfLogType.WAKE).build());

        saveAndSend(game, playerSet);
        broadcastVoiceLineNotification(getVoiceLineNotification(voiceLineType, Arrays.stream(players).map(Player::getCodeName).toArray(CodeName[]::new)));
    }

    private void processNightPostVote(Set<WerewolfPlayer> playerSet, WerewolfVoiceLineType voiceLineType, WerewolfPlayer... players) {
        WerewolfGame game = this.getGame();
        game.getLog().add(getLogEntryTemplate(playerSet).logType(WerewolfLogType.SLEEP).build());

        saveAndSend(game, playerSet);
        broadcastVoiceLineNotification(getVoiceLineNotification(voiceLineType, Arrays.stream(players).map(Player::getCodeName).toArray(CodeName[]::new)));
        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);
    }

    private void handlePlayerDiedEvent(WerewolfPlayer target) {
        WerewolfGame game = getGame();
        WerewolfPlayer targetInDatabase = game.getPlayers().stream().filter(werewolfPlayer -> werewolfPlayer.getId().equals(target.getId())).findFirst().orElseThrow();

        if (targetInDatabase.getPlayerState() == PlayerState.ALIVE) {
            targetInDatabase.setPlayerState(PlayerState.DECEASED);
            game.getLog().add(getPlayerDiedLogEntry(targetInDatabase));
            saveAndBroadcast(game);
            broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.DEATH, target.getCodeName()));
            pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);
            checkGameEnded();
        }
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
