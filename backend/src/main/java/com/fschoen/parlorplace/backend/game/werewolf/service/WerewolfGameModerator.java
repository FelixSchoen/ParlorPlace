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
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayerVoteCollection;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayerWerewolfVote;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.BodyguardWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.CupidWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.WitchWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfFaction;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfLogType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoiceLineType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteDescriptor;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteIdentifier;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfLogEntryRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfPlayerRepository;
import com.fschoen.parlorplace.backend.game.werewolf.utility.WerewolfValueIdentifier;
import com.fschoen.parlorplace.backend.game.werewolf.utility.WerewolfVoiceLineClientNotification;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.service.FakeService;
import com.fschoen.parlorplace.backend.service.game.AbstractGameModerator;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import com.fschoen.parlorplace.backend.utility.other.SetBuilder;
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
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
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

    private final WerewolfPlayerWerewolfVoteService playerVoteService;
    private final FakeService fakeService;

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("values.werewolf-values");

    private final long WAIT_TIME_SOCKETS_ESTABLISHED = Long.parseLong(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_SOCKETS_ESTABLISHED));
    private final long WAIT_TIME_INITIAL_ROLES = Long.parseLong(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_INITIAL_ROLES));
    private final long WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS = Long.parseLong(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS));
    private final long WAIT_TIME_SHORT_BUFFER = Long.parseLong(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_SHORT_BUFFER));
    private final long WAIT_TIME_NEW_INFORMATION = Long.parseLong(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_NEW_INFORMATION));

    private final long VOTE_TIME_INDIVIDUAL_VOTE = Long.parseLong(resourceBundle.getString(WerewolfValueIdentifier.VOTE_TIME_INDIVIDUAL_VOTE));
    private final long VOTE_TIME_GROUP_VOTE = Long.parseLong(resourceBundle.getString(WerewolfValueIdentifier.VOTE_TIME_GROUP_VOTE));
    private final long VOTE_TIME_ALL_VOTE = Long.parseLong(resourceBundle.getString(WerewolfValueIdentifier.VOTE_TIME_ALL_VOTE));

    @Autowired
    public WerewolfGameModerator(WerewolfPlayerWerewolfVoteService playerVoteService, FakeService fakeService, CommunicationService communicationService, UserRepository userRepository, WerewolfGameRepository gameRepository, WerewolfPlayerRepository playerRepository, WerewolfLogEntryRepository logEntryRepository) {
        super(communicationService, userRepository, gameRepository, playerRepository, logEntryRepository);
        this.fakeService = fakeService;
        this.playerVoteService = playerVoteService;
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
            // Check if selection of roles makes game end instantly

            checkGameEnded();

            while (true) {
                processTransitionNight();
                processNight();
                processTransitionDay();
                processDay();
            }
        } catch (GameEndException e) {
            log.info("Game {} sent GameEndException", this.gameIdentifier.getToken());
        } catch (Exception e) {
            log.error("Exception in Game {} has occurred", this.gameIdentifier.getToken(), e);
        }

        // End the game and determine placements of players
        game = getGame();
        game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.END).build());
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.END));
        game = saveAndBroadcast(game);

        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);

        setPlacements(game);
        game.setGameState(GameState.CONCLUDED);
        game.setEndedAt(Instant.now());
        game = this.gameRepository.save(game);
        sendGameEndedNotification(gameIdentifier, game.getPlayers());

        log.info("Concluded Game {}", this.gameIdentifier.getToken());
    }

    // -------------
    // --- Night ---
    // -------------

    private void processTransitionNight() throws ExecutionException, InterruptedException {
        WerewolfGame game = this.getGame();

        // Process Village Kill
        if (game.getRound() != 0) {
            for (WerewolfPlayerWerewolfVote vote : getVotesInRoundOfVoteDescriptor(getCurrentRound(), WerewolfVoteDescriptor.VILLAGERS_LYNCH)) {
                for (WerewolfPlayer target : vote.getOutcome()) {
                    playerDeathEvent(target);
                }
            }
        }

        game = this.getGame();

        game.setRound(game.getRound() + 1);
        game.getLog().add(getLogEntryTemplate(getAlivePlayers()).logType(WerewolfLogType.SLEEP).build());

        saveAndBroadcast(game);
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.VILLAGE_SLEEP));
        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);
    }

    private void processNight() throws ExecutionException, InterruptedException {
        processAllCupids();
        processAllBodyguards();
        processWerewolves();
        processAllWitches();
        processAllSeers();
    }

    private void processAllCupids() throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> cupids = getAlivePlayersOfLastRoleType(WerewolfRoleType.CUPID);
        for (WerewolfPlayer cupid : cupids)
            if (!((CupidWerewolfGameRole) getLastRole(cupid)).hasLinked())
                processCupid(cupid);
    }

    private void processCupid(WerewolfPlayer cupid) throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> cupidSet = new SetBuilder<WerewolfPlayer>().add(cupid).build();

        processNightPreVote(cupidSet, WerewolfVoiceLineType.CUPID_WAKE, cupid);

        // --- Logic Start ---

        Set<WerewolfPlayer> validLinkTargets = getAlivePlayers();

        CompletableFuture<WerewolfPlayerWerewolfVote> cupidVoteFuture = this.playerVoteService.requestVote(
                this.gameIdentifier,
                VoteType.PRIVATE_PUBLIC_PUBLIC,
                VoteDrawStrategy.CHOOSE_RANDOM,
                2,
                this.playerVoteService.getSameChoiceCollectionMap(
                        cupidSet,
                        validLinkTargets,
                        2,
                        false),
                WerewolfVoteDescriptor.CUPID_LINK,
                getCurrentRound(),
                VOTE_TIME_INDIVIDUAL_VOTE
        );
        WerewolfPlayerWerewolfVote cupidVote = cupidVoteFuture.get();

        Set<WerewolfPlayer> cupidTargets = cupidVote.getOutcome();
        CupidWerewolfGameRole cupidWerewolfGameRole = (CupidWerewolfGameRole) getLastRole(cupid);
        cupidWerewolfGameRole.hasLinked(true);
        save(cupid);

        WerewolfFaction faction = getLastRole(cupidTargets.stream().findAny().orElseThrow(
                () -> new GameException(Messages.exception(MessageIdentifier.ROLE_EXISTS_NOT)))).getWerewolfFaction();
        if (!cupidTargets.stream().allMatch(target -> getLastRole(target).getWerewolfFaction() == faction)) {
            // Lovers belong to different factions

            for (WerewolfPlayer lover : cupidTargets) {
                getLastRole(lover).setWerewolfFaction(WerewolfFaction.LOVERS);
                save(lover);
            }
        }

        WerewolfGame game = this.getGame();
        game.getLog().add(getLogEntryTemplate(cupidSet).logType(WerewolfLogType.CUPID_LINK).targets(cupidTargets).build());
        saveAndSend(game, cupidSet);

        processNightPostVote(cupidSet, WerewolfVoiceLineType.PLAYER_SLEEP, cupid);

        // Notice Lovers

        processNightPreVote(cupidTargets, WerewolfVoiceLineType.LOVERS_WAKE);
        sendPlayerNotificationNotification(cupidTargets);

        pause(WAIT_TIME_SHORT_BUFFER);

        WerewolfPlayer[] cupidTargetsArray = cupidTargets.toArray(WerewolfPlayer[]::new);

        game = this.getGame();
        game.getLog().add(getLogEntryTemplate(new SetBuilder<WerewolfPlayer>().add(cupidTargetsArray[0]).build())
                .logType(WerewolfLogType.LOVERS_LOVE).targets(new SetBuilder<WerewolfPlayer>().add(cupidTargetsArray[1]).build()).build());
        game.getLog().add(getLogEntryTemplate(new SetBuilder<WerewolfPlayer>().add(cupidTargetsArray[1]).build())
                .logType(WerewolfLogType.LOVERS_LOVE).targets(new SetBuilder<WerewolfPlayer>().add(cupidTargetsArray[0]).build()).build());
        saveAndSend(game, cupidTargets);

        pause(WAIT_TIME_NEW_INFORMATION);

        processNightPostVote(cupidTargets, WerewolfVoiceLineType.LOVERS_SLEEP);
    }

    private void processAllBodyguards() throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> bodyguards = getAlivePlayersOfLastRoleType(WerewolfRoleType.BODYGUARD);
        for (WerewolfPlayer bodyguard : bodyguards)
            processBodyguard(bodyguard);
    }

    private void processBodyguard(WerewolfPlayer bodyguard) throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> bodyguardSet = new SetBuilder<WerewolfPlayer>().add(bodyguard).build();

        processNightPreVote(bodyguardSet, WerewolfVoiceLineType.BODYGUARD_WAKE, bodyguard);

        BodyguardWerewolfGameRole bodyguardWerewolfGameRole = (BodyguardWerewolfGameRole) getLastRole(bodyguard);
        Set<WerewolfPlayer> validProtectTargets = getAlivePlayers();
        validProtectTargets.remove(bodyguardWerewolfGameRole.getLastProtected());

        CompletableFuture<WerewolfPlayerWerewolfVote> bodyguardVoteFuture = this.playerVoteService.requestVote(
                this.gameIdentifier,
                VoteType.PRIVATE_PUBLIC_PUBLIC,
                VoteDrawStrategy.CHOOSE_RANDOM,
                1,
                this.playerVoteService.getSameChoiceCollectionMap(
                        bodyguardSet,
                        validProtectTargets,
                        1,
                        false),
                WerewolfVoteDescriptor.BODYGUARD_PROTECT,
                getCurrentRound(),
                VOTE_TIME_INDIVIDUAL_VOTE
        );
        WerewolfPlayerWerewolfVote bodyguardProtectVote = bodyguardVoteFuture.get();

        // --- Logic Start ---

        WerewolfPlayer bodyguardProtectTarget = bodyguardProtectVote.getOutcome().stream().findAny().orElseThrow(
                () -> new GameException(Messages.exception(MessageIdentifier.VOTE_OUTCOME_EXISTS_NOT))
        );
        bodyguardWerewolfGameRole.setLastProtected(bodyguardProtectTarget);
        WerewolfGame game = save(bodyguard);

        game.getLog().add(getLogEntryTemplate(bodyguardSet).logType(WerewolfLogType.BODYGUARD_PROTECT)
                .targets(new SetBuilder<WerewolfPlayer>().add(bodyguardProtectTarget).build()).build());
        saveAndSend(game, bodyguardSet);

        // --- Logic End ---

        processNightPostVote(bodyguardSet, WerewolfVoiceLineType.PLAYER_SLEEP, bodyguard);
    }

    private void processWerewolves() throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> werewolves = getAlivePlayersOfLastRoleType(WerewolfRoleType.WEREWOLF);
        Set<WerewolfPlayer> validTargets = getAlivePlayers();

        processNightPreVote(werewolves, WerewolfVoiceLineType.WEREWOLVES_WAKE);

        CompletableFuture<WerewolfPlayerWerewolfVote> werewolfVoteFuture = this.playerVoteService.requestVote(
                this.gameIdentifier,
                VoteType.PRIVATE_PUBLIC_PUBLIC,
                VoteDrawStrategy.CHOOSE_RANDOM,
                1,
                this.playerVoteService.getSameChoiceCollectionMap(
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
        Set<WerewolfPlayer> witchSet = new SetBuilder<WerewolfPlayer>().add(witch).build();

        processNightPreVote(witchSet, WerewolfVoiceLineType.WITCH_WAKE, witch);

        // --- Logic Start ---

        WitchWerewolfGameRole witchWerewolfGameRole = (WitchWerewolfGameRole) getLastRole(witch);

        boolean hasHealed = witchWerewolfGameRole.hasHealed();
        boolean hasKilled = witchWerewolfGameRole.hasKilled();

        if (!hasHealed) {
            Set<WerewolfPlayer> validHealTargets = new HashSet<>();
            getVotesInRoundOfVoteDescriptor(getCurrentRound(), WerewolfVoteDescriptor.WEREWOLVES_KILL).forEach(vote -> validHealTargets.addAll(vote.getOutcome()));

            CompletableFuture<WerewolfPlayerWerewolfVote> witchHealVoteFuture = this.playerVoteService.requestVote(
                    this.gameIdentifier,
                    VoteType.PRIVATE_PUBLIC_PUBLIC,
                    VoteDrawStrategy.HARD_NO_OUTCOME,
                    1,
                    this.playerVoteService.getSameChoiceCollectionMap(
                            witchSet,
                            validHealTargets,
                            1,
                            true),
                    WerewolfVoteDescriptor.WITCH_HEAL,
                    getCurrentRound(),
                    VOTE_TIME_INDIVIDUAL_VOTE
            );
            WerewolfPlayerWerewolfVote witchHealVote = witchHealVoteFuture.get();

            if (witchHealVote.getOutcome().size() > 0) {
                WerewolfPlayer witchHealTarget = witchHealVote.getOutcome().stream().findAny().orElseThrow(
                        () -> new GameException(Messages.exception(MessageIdentifier.VOTE_OUTCOME_EXISTS_NOT))
                );
                witchWerewolfGameRole.hasHealed(true);
                WerewolfGame game = save(witch);

                game.getLog().add(getLogEntryTemplate(witchSet).logType(WerewolfLogType.WITCH_HEAL).targets(new SetBuilder<WerewolfPlayer>().add(witchHealTarget).build()).build());
                saveAndSend(game, witchSet);
            }
        }

        if (!hasKilled) {
            Set<WerewolfPlayer> validKillTargets = getAlivePlayers();

            CompletableFuture<WerewolfPlayerWerewolfVote> witchKillVoteFuture = this.playerVoteService.requestVote(
                    this.gameIdentifier,
                    VoteType.PRIVATE_PUBLIC_PUBLIC,
                    VoteDrawStrategy.HARD_NO_OUTCOME,
                    1,
                    this.playerVoteService.getSameChoiceCollectionMap(
                            witchSet,
                            validKillTargets,
                            1,
                            true),
                    WerewolfVoteDescriptor.WITCH_KILL,
                    getCurrentRound(),
                    VOTE_TIME_INDIVIDUAL_VOTE
            );
            WerewolfPlayerWerewolfVote witchKillVote = witchKillVoteFuture.get();

            if (witchKillVote.getOutcome().size() > 0) {
                WerewolfPlayer witchKillTarget = witchKillVote.getOutcome().stream().findAny().orElseThrow(
                        () -> new GameException(Messages.exception(MessageIdentifier.VOTE_OUTCOME_EXISTS_NOT))
                );
                witchWerewolfGameRole.hasKilled(true);
                WerewolfGame game = save(witch);

                game.getLog().add(getLogEntryTemplate(witchSet).logType(WerewolfLogType.WITCH_KILL)
                        .targets(new SetBuilder<WerewolfPlayer>().add(witchKillTarget).build()).build());
                saveAndSend(game, witchSet);
            }
        }

        // Fake Actions
        if (hasHealed)
            this.fakeService.fakeTimeND(VOTE_TIME_INDIVIDUAL_VOTE/2, WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS, VOTE_TIME_INDIVIDUAL_VOTE);
        if (hasKilled)
            this.fakeService.fakeTimeND(VOTE_TIME_INDIVIDUAL_VOTE/2, WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS, VOTE_TIME_INDIVIDUAL_VOTE);

        // --- Logic End ---

        processNightPostVote(witchSet, WerewolfVoiceLineType.PLAYER_SLEEP, witch);
    }

    private void processAllSeers() throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> seers = getAlivePlayersOfLastRoleType(WerewolfRoleType.SEER);
        for (WerewolfPlayer seer : seers)
            processSeer(seer);
    }

    private void processSeer(WerewolfPlayer seer) throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> seerSet = new SetBuilder<WerewolfPlayer>().add(seer).build();

        processNightPreVote(seerSet, WerewolfVoiceLineType.SEER_WAKE, seer);

        Set<WerewolfPlayer> validSeeTargets = getAlivePlayers();
        validSeeTargets.remove(seer);

        CompletableFuture<WerewolfPlayerWerewolfVote> seerVoteFuture = this.playerVoteService.requestVote(
                this.gameIdentifier,
                VoteType.PRIVATE_PUBLIC_PUBLIC,
                VoteDrawStrategy.CHOOSE_RANDOM,
                1,
                this.playerVoteService.getSameChoiceCollectionMap(
                        seerSet,
                        validSeeTargets,
                        1,
                        false),
                WerewolfVoteDescriptor.SEER_SEE,
                getCurrentRound(),
                VOTE_TIME_INDIVIDUAL_VOTE
        );
        WerewolfPlayerWerewolfVote seerVote = seerVoteFuture.get();

        WerewolfPlayer seerTarget = seerVote.getOutcome().stream().findFirst().orElseThrow(
                () -> new GameException(Messages.exception(MessageIdentifier.VOTE_OUTCOME_EXISTS_NOT))
        );

        // --- Logic Start ---

        WerewolfGame game = this.getGame();

        if (hasLastRoleType(seerTarget, WerewolfRoleType.WEREWOLF) || hasLastRoleType(seerTarget, WerewolfRoleType.LYCANTHROPE)) {
            game.getLog().add(getLogEntryTemplate(seerSet).logType(WerewolfLogType.SEER_SUCCESS)
                    .targets(new SetBuilder<WerewolfPlayer>().add(seerTarget).build()).build());

        } else {
            game.getLog().add(getLogEntryTemplate(seerSet).logType(WerewolfLogType.SEER_FAILURE).targets(new SetBuilder<WerewolfPlayer>().add(seerTarget).build()).build());
        }

        saveAndSend(game, seerSet);
        pause(WAIT_TIME_NEW_INFORMATION);

        // --- Logic End ---

        processNightPostVote(seerSet, WerewolfVoiceLineType.PLAYER_SLEEP, seer);
    }

    // -----------
    // --- Day ---
    // -----------

    private void processTransitionDay() throws ExecutionException, InterruptedException {
        WerewolfGame game = this.getGame();
        game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.WAKE).build());

        saveAndBroadcast(game);
        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.VILLAGE_WAKE));
        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);

        // Process Werewolf Kill
        for (WerewolfPlayerWerewolfVote playerVote : getVotesInRoundOfVoteDescriptor(getCurrentRound(), WerewolfVoteDescriptor.WEREWOLVES_KILL)) {
            for (WerewolfPlayer target : playerVote.getOutcome()) {
                // Witch
                Set<WerewolfPlayer> witchHealTargets = new HashSet<>();
                getVotesInRoundOfVoteDescriptor(getCurrentRound(), WerewolfVoteDescriptor.WITCH_HEAL).forEach(witchHealVote -> witchHealTargets.addAll(witchHealVote.getOutcome()));

                // Bodyguard
                Set<WerewolfPlayer> bodyguardProtectTargets = new HashSet<>();
                getVotesInRoundOfVoteDescriptor(getCurrentRound(), WerewolfVoteDescriptor.BODYGUARD_PROTECT).forEach(bodyguardProtectVote -> bodyguardProtectTargets.addAll(bodyguardProtectVote.getOutcome()));

                if (!witchHealTargets.contains(target) && !bodyguardProtectTargets.contains(target))
                    playerDeathEvent(target);
            }
        }

        // Process Witch Kill
        for (WerewolfPlayerWerewolfVote witchKillVote : getVotesInRoundOfVoteDescriptor(getCurrentRound(), WerewolfVoteDescriptor.WITCH_KILL)) {
            for (WerewolfPlayer target : witchKillVote.getOutcome()) {
                playerDeathEvent(target);
            }
        }
    }

    private void processDay() throws ExecutionException, InterruptedException {
        processAllBearTamers();
        processVillagers();
    }

    private void processAllBearTamers() {
        Set<WerewolfPlayer> bearTamers = getAlivePlayersOfLastRoleType(WerewolfRoleType.BEAR_TAMER);
        for (WerewolfPlayer bearTamer : bearTamers)
            processBearTamer(bearTamer);
    }

    private void processBearTamer(WerewolfPlayer bearTamer) {
        Set<WerewolfPlayer> bearTamerSet = new SetBuilder<WerewolfPlayer>().add(bearTamer).build();

        // --- Logic Start ---

        WerewolfGame game = this.getGame();
        Set<WerewolfPlayer> neighbours = getAliveNeighbours(bearTamer);

        if (neighbours.stream().anyMatch(
                neighbour -> hasLastRoleType(neighbour, WerewolfRoleType.WEREWOLF) || hasLastRoleType(neighbour, WerewolfRoleType.LYCANTHROPE))) {
            game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.BEAR_TAMER_GROWL).build());
            broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.BEAR_TAMER_GROWL,
                    bearTamerSet.stream().map(Player::getCodeName).toArray(CodeName[]::new)));
        } else {
            game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.BEAR_TAMER_SILENT).build());
            broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.BEAR_TAMER_SILENT,
                    bearTamerSet.stream().map(Player::getCodeName).toArray(CodeName[]::new)));
        }

        saveAndBroadcast(game);
        pause(WAIT_TIME_NEW_INFORMATION);

        // --- Logic End ---
    }

    private void processVillagers() throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> villagers = getAlivePlayers();
        Set<WerewolfPlayer> validTargets = getAlivePlayers();

        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.VILLAGE_VOTE));

        for (int x = 0; x < 2; x++) {
            Map<Long, WerewolfPlayerVoteCollection> voteCollectionMap = this.playerVoteService.getSameChoiceCollectionMap(
                    villagers,
                    validTargets,
                    1,
                    true);

            // Remove lovers from possible options
            for (Map.Entry<Long, WerewolfPlayerVoteCollection> entry : voteCollectionMap.entrySet()) {
                WerewolfPlayer voter = this.playerRepository.findOneById(entry.getKey()).orElseThrow(
                        () -> new GameException(Messages.exception(MessageIdentifier.PLAYER_EXISTS_NOT))
                );
                Set<WerewolfPlayer> voterLovers = getLoved(voter);
                entry.getValue().getSubjects().removeAll(voterLovers);
            }

            CompletableFuture<WerewolfPlayerWerewolfVote> werewolfVoteFuture = this.playerVoteService.requestVote(
                    this.gameIdentifier,
                    VoteType.PUBLIC_PUBLIC_PUBLIC,
                    VoteDrawStrategy.HARD_NO_OUTCOME,
                    1,
                    voteCollectionMap,
                    WerewolfVoteDescriptor.VILLAGERS_LYNCH,
                    getCurrentRound(),
                    VOTE_TIME_ALL_VOTE / (x + 1)
            );
            WerewolfPlayerWerewolfVote villagerVote = werewolfVoteFuture.get();

            // Give second chance for vote
            if (villagerVote.getOutcome().size() > 0)
                break;
        }

        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);
    }

    // ---------------------
    // --- Indeterminate ---
    // ---------------------

    private void processHunter(WerewolfPlayer hunter) throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> hunterSet = new SetBuilder<WerewolfPlayer>().add(hunter).build();

        broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.HUNTER_ACTION, hunterSet.stream().map(Player::getCodeName).toArray(CodeName[]::new)));

        Set<WerewolfPlayer> validHunterTargets = getAlivePlayers();

        CompletableFuture<WerewolfPlayerWerewolfVote> hunterVoteFuture = this.playerVoteService.requestVote(
                this.gameIdentifier,
                VoteType.PRIVATE_PUBLIC_PUBLIC,
                VoteDrawStrategy.CHOOSE_RANDOM,
                1,
                this.playerVoteService.getSameChoiceCollectionMap(
                        hunterSet,
                        validHunterTargets,
                        1,
                        false),
                WerewolfVoteDescriptor.HUNTER_SHOOT,
                getCurrentRound(),
                VOTE_TIME_INDIVIDUAL_VOTE
        );
        WerewolfPlayerWerewolfVote hunterVote = hunterVoteFuture.get();

        WerewolfPlayer hunterTarget = hunterVote.getOutcome().stream().findFirst().orElseThrow(
                () -> new GameException(Messages.exception(MessageIdentifier.VOTE_OUTCOME_EXISTS_NOT))
        );

        WerewolfGame game = this.getGame();
        game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.HUNTER_SHOOT)
                .sources(new SetBuilder<WerewolfPlayer>().add(hunter).build())
                .targets(new SetBuilder<WerewolfPlayer>().add(hunterTarget).build()).build());

        saveAndBroadcast(game);
        pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);

        playerDeathEvent(hunterTarget);
    }

    private void processPotentialLovers(WerewolfPlayer player) throws ExecutionException, InterruptedException {
        Set<WerewolfPlayer> lovers = getLoved(player);
        for (WerewolfPlayer lover : lovers) {
            if (lover.getPlayerState() == PlayerState.ALIVE) {
                playerDeathEvent(lover);
            }
        }
    }

    // --------------
    // --- Events ---
    // --------------

    private void playerDeathEvent(WerewolfPlayer target) throws ExecutionException, InterruptedException {
        WerewolfGame game = getGame();
        WerewolfPlayer targetInDatabase = game.getPlayers().stream().filter(werewolfPlayer -> werewolfPlayer.getId().equals(target.getId())).findFirst().orElseThrow(
                () -> new GameException(Messages.exception(MessageIdentifier.PLAYER_EXISTS_NOT))
        );

        if (targetInDatabase.getPlayerState() == PlayerState.ALIVE) {
            targetInDatabase.setPlayerState(PlayerState.DECEASED);
            game.getLog().add(getPlayerDiedLogEntry(targetInDatabase));

            saveAndBroadcast(game);
            broadcastVoiceLineNotification(getVoiceLineNotification(WerewolfVoiceLineType.DEATH, target.getCodeName()));

            pause(WAIT_TIME_BETWEEN_CONSECUTIVE_EVENTS);
            checkGameEnded();

            // Hunter
            if (hasLastRoleType(target, WerewolfRoleType.HUNTER))
                processHunter(target);

            // Lovers
            processPotentialLovers(targetInDatabase);
        }
    }

    // ---------------
    // --- Utility ---
    // ---------------

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

    // Utility - Win Conditions

    private void checkGameEnded() {
        if (checkVillagersWin()
                || checkWerewolvesWin()
                || checkLoversWin()) {
            log.info("Game {} has ended. Villagers win: {}, Werewolves win: {}, Lovers win: {}", this.gameIdentifier.getToken(), checkVillagersWin(), checkWerewolvesWin(), checkLoversWin());
            throw new GameEndException();
        }
    }

    private Boolean checkVillagersWin() {
        return getAlivePlayersOfLastRoleType(WerewolfRoleType.WEREWOLF).isEmpty()
                && getAlivePlayersOfFaction(WerewolfFaction.LOVERS).isEmpty();
    }

    private Boolean checkWerewolvesWin() {
        AtomicInteger maxAliveVillagersNeededForWin = new AtomicInteger(
                getAlivePlayersOfFaction(WerewolfFaction.VILLAGERS).size()
                        + getAlivePlayersOfFaction(WerewolfFaction.LOVERS).size());

        // Witch
        getAlivePlayersOfLastRoleType(WerewolfRoleType.WITCH).stream()
                .filter(witch -> !((WitchWerewolfGameRole) getLastRole(witch)).hasKilled() && !((WitchWerewolfGameRole) getLastRole(witch)).hasHealed())
                .forEach(witch -> maxAliveVillagersNeededForWin.getAndIncrement());

        return maxAliveVillagersNeededForWin.get() <= getAlivePlayersOfFaction(WerewolfFaction.WEREWOLVES).size();
    }

    private Boolean checkLoversWin() {
        return getAlivePlayers().stream().allMatch(player -> getLastRole(player).getWerewolfFaction() == WerewolfFaction.LOVERS)
                && getAlivePlayers().size() == 2;
    }

    private void setPlacements(WerewolfGame game) {
        Set<WerewolfPlayer> players = game.getPlayers();

        int winners = 0;

        for (WerewolfPlayer player : players) {
            if (getLastRole(player).getWerewolfFaction() == WerewolfFaction.VILLAGERS && checkVillagersWin()
                    || getLastRole(player).getWerewolfFaction() == WerewolfFaction.WEREWOLVES && checkWerewolvesWin()
                    || getLastRole(player).getWerewolfFaction() == WerewolfFaction.LOVERS && checkLoversWin() && player.getPlayerState() == PlayerState.ALIVE) {
                player.setPlacement(1);
                winners++;
            }

        }

        int finalWinners = winners;
        players.stream().filter(player -> player.getPlacement() == null).forEach(player -> player.setPlacement(finalWinners + 1));
    }

    // Utility - Communication

    private WerewolfLogEntry.WerewolfLogEntryBuilder<?, ?> getLogEntryTemplate(Set<WerewolfPlayer> recipients) {
        return WerewolfLogEntry.builder().identifier(UUID.randomUUID()).game(getActiveGame(this.gameIdentifier)).recipients(recipients);
    }

    private WerewolfLogEntry getPlayerDiedLogEntry(WerewolfPlayer werewolfPlayer) {
        return getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.DEATH).targets(new SetBuilder<WerewolfPlayer>().add(werewolfPlayer).build()).build();
    }

    private WerewolfVoiceLineClientNotification getVoiceLineNotification(WerewolfVoiceLineType voiceLineType, CodeName... codeNames) {
        Set<CodeName> codeNamesSet = new HashSet<>(List.of(codeNames));
        return WerewolfVoiceLineClientNotification.builder().voiceLineType(voiceLineType).codeNames(codeNamesSet).build();
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

    private Set<WerewolfPlayerWerewolfVote> getPlayerVotesOfVoteDescriptor(WerewolfVoteDescriptor descriptor) {
        WerewolfGame game = getGame();
        return game.getVotes().stream().filter(vote -> (vote.getVoteDescriptor() == descriptor) && (vote.getVoteIdentifier() == WerewolfVoteIdentifier.PLAYER_VOTE))
                .map(v -> (WerewolfPlayerWerewolfVote) v).collect(Collectors.toSet());
    }

    private Set<WerewolfPlayerWerewolfVote> getVotesInRoundOfVoteDescriptor(Integer round, WerewolfVoteDescriptor descriptor) {
        return this.getPlayerVotesOfVoteDescriptor(descriptor).stream().filter(vote -> Objects.equals(vote.getRound(), round)).collect(Collectors.toSet());
    }

    private Set<WerewolfPlayer> getLoved(WerewolfPlayer player) {
        Set<WerewolfPlayer> lovers = new HashSet<>();
        Set<WerewolfPlayerWerewolfVote> cupidVotes = getPlayerVotesOfVoteDescriptor(WerewolfVoteDescriptor.CUPID_LINK);

        for (WerewolfPlayerWerewolfVote cupidVote : cupidVotes) {
            if (cupidVote.getOutcome().contains(player)) {
                lovers.addAll(cupidVote.getOutcome());
            }
        }

        lovers.remove(player);
        return lovers;
    }


}
