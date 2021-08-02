package com.fschoen.parlorplace.backend.game.werewolf.management;

import com.fschoen.parlorplace.backend.entity.RuleSet;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.game.management.GameInstance;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.GameCoordinationService;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifiers;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class WerewolfInstance extends GameInstance<WerewolfGame, WerewolfPlayer, WerewolfGameRepository, WerewolfRuleSet> {

    @Autowired
    public WerewolfInstance(GameCoordinationService gameCoordinationService, WerewolfGameRepository gameRepository, UserRepository userRepository, WerewolfManager werewolfManager) {
        super(WerewolfGame.class, WerewolfPlayer.class, WerewolfRuleSet.class, gameCoordinationService, gameRepository, userRepository,werewolfManager, log);
    }

    @Override
    public WerewolfGame changeLobby(RuleSet ruleSet) {
        if (!(ruleSet instanceof WerewolfRuleSet))
            throw new DataConflictException(Messages.exception(MessageIdentifiers.GAME_TYPE_MISMATCH));

        WerewolfGame game = getGame();

        game.setRuleSet((WerewolfRuleSet) ruleSet);

        return gameRepository.save(game);
    }

}
