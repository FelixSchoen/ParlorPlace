package com.fschoen.parlorplace.backend.game.werewolf.service.game;

import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.SeerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.VillagerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.WerewolfWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfGamePhase;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.AbstractGameService;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.service.GameIdentifierService;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class WerewolfGameService extends AbstractGameService<
        WerewolfGame,
        WerewolfPlayer,
        WerewolfRuleSet,
        WerewolfGameRole,
        WerewolfGameRepository,
        WerewolfGameModerator
        > {

    private static final Map<WerewolfRoleType, Class<? extends WerewolfGameRole>> werewolfGameRoleClasses = new HashMap<>() {{
        put(WerewolfRoleType.VILLAGER, VillagerWerewolfGameRole.class);
        put(WerewolfRoleType.WEREWOLF, WerewolfWerewolfGameRole.class);
        put(WerewolfRoleType.SEER, SeerWerewolfGameRole.class);
    }};

    @Autowired
    public WerewolfGameService(
            UserRepository userRepository,
            CommunicationService communicationService,
            GameIdentifierService gameIdentifierService,
            WerewolfGameRepository gameRepository,
            @Qualifier("taskExecutor") TaskExecutor taskExecutor,
            ApplicationContext applicationContext
    ) {
        super(userRepository, communicationService, gameIdentifierService, gameRepository, taskExecutor, applicationContext);
    }

    @Override
    protected Class<WerewolfGame> getGameClass() {
        return WerewolfGame.class;
    }

    @Override
    protected Class<WerewolfPlayer> getPlayerClass() {
        return WerewolfPlayer.class;
    }

    @Override
    protected Class<WerewolfRuleSet> getRuleSetClass() {
        return WerewolfRuleSet.class;
    }

    @Override
    protected Class<WerewolfGameModerator> getModeratorClass() {
        return WerewolfGameModerator.class;
    }

    @Override
    protected WerewolfGame onInitializeGame(WerewolfGame game) {
        game.setGamePhase(WerewolfGamePhase.START_OF_ROUND);
        return game;
    }

    @Override
    protected WerewolfGame onGameStart(WerewolfGame game) {
        int expectedRoles = game.getPlayers().size();
        int actualRoles = game.getRuleSet().getGameRoleTypes().size();

        if (expectedRoles > actualRoles)
            throw new GameException(Messages.exception(MessageIdentifier.GAME_ROLES_AMOUNT_MISMATCH));

        // Assign roles

        List<WerewolfRoleType> availableRoles = new ArrayList<>(game.getRuleSet().getGameRoleTypes());

        Random random = new Random();
        try {
            for (WerewolfPlayer p : game.getPlayers()) {
                WerewolfRoleType role = availableRoles.get(random.nextInt(availableRoles.size()));
                WerewolfGameRole gameRole = werewolfGameRoleClasses.get(role).getDeclaredConstructor().newInstance();
                gameRole.setPlayer(p);
                p.getGameRoles().add(gameRole);
                availableRoles.remove(role);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataConflictException(Messages.exception(MessageIdentifier.ROLE_TYPE_MISMATCH));
        }

        return game;
    }
}