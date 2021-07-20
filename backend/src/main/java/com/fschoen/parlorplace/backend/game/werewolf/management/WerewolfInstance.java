package com.fschoen.parlorplace.backend.game.werewolf.management;

import com.fschoen.parlorplace.backend.entity.persistance.RuleSet;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.game.management.GameInstance;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfPlayerRepository;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.PlayerRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.GameService;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashSet;

@Component
@Scope("prototype")
@Slf4j
public class WerewolfInstance extends GameInstance<WerewolfGame, WerewolfPlayer, WerewolfGameRepository, WerewolfRuleSet> {

    @Autowired
    public WerewolfInstance(GameService gameService, WerewolfGameRepository gameRepository, UserRepository userRepository, WerewolfManager werewolfManager) {
        super(WerewolfGame.class, WerewolfPlayer.class, WerewolfRuleSet.class, gameService, gameRepository, userRepository,werewolfManager, log);
    }

    @Override
    public WerewolfGame changeLobby(RuleSet ruleSet) {
        if (!(ruleSet instanceof WerewolfRuleSet))
            throw new DataConflictException(Messages.exception("game.type.mismatch"));

        WerewolfGame game = getGame();

        game.setRuleSet(ruleSet);

        return gameRepository.save(game);
    }
}
