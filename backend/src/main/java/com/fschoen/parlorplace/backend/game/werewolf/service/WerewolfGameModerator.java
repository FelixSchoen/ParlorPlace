package com.fschoen.parlorplace.backend.game.werewolf.service;

import com.fschoen.parlorplace.backend.enumeration.CodeName;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfLogEntry;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfLogType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoiceLineType;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfLogEntryRepository;
import com.fschoen.parlorplace.backend.game.werewolf.utility.WerewolfValueIdentifier;
import com.fschoen.parlorplace.backend.game.werewolf.utility.WerewolfVoiceLineClientNotification;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.AbstractGameModerator;
import com.fschoen.parlorplace.backend.service.CommunicationService;
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
    private static final int WAIT_TIME_INITIAL_ROLES = Integer.parseInt(resourceBundle.getString(WerewolfValueIdentifier.WAIT_TIME_INITIAL_ROLES));

    @Autowired
    public WerewolfGameModerator(CommunicationService communicationService, UserRepository userRepository, WerewolfGameRepository gameRepository, WerewolfLogEntryRepository logEntryRepository) {
        super(communicationService, userRepository, gameRepository, logEntryRepository);
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
        saveAndBroadcast(game);

        broadcastVoiceLineNotification(WerewolfVoiceLineClientNotification.builder().voiceLineType(WerewolfVoiceLineType.START).build());

        Thread.sleep(WAIT_TIME_INITIAL_ROLES);

        while (this.isGameOngoing()) {
            processTransitionNight();
            break;
        }
    }

    private void processTransitionNight() {
        WerewolfGame game = this.getGame();
        game.getLog().add(getLogEntryTemplate(getAllPlayersOfGame()).logType(WerewolfLogType.SLEEP).build());
        saveAndBroadcast(game);
    }

    // Utility

    private WerewolfLogEntry.WerewolfLogEntryBuilder<?, ?> getLogEntryTemplate(Set<WerewolfPlayer> recipients) {
        return WerewolfLogEntry.builder().identifier(UUID.randomUUID()).game(getActiveGame(this.gameIdentifier)).recipients(recipients);
    }

    private WerewolfVoiceLineClientNotification getVoiceLineNotification(WerewolfVoiceLineType voiceLineType, CodeName... codeNames) {
        Set<CodeName> codeNamesSet = new HashSet<>(List.of(codeNames));
        return WerewolfVoiceLineClientNotification.builder().voiceLineType(voiceLineType).codeNames(codeNamesSet).build();
    }



}
