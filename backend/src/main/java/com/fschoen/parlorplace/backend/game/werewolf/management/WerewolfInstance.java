package com.fschoen.parlorplace.backend.game.werewolf.management;

import com.fschoen.parlorplace.backend.entity.persistance.*;
import com.fschoen.parlorplace.backend.exception.*;
import com.fschoen.parlorplace.backend.game.management.*;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.*;
import com.fschoen.parlorplace.backend.game.werewolf.repository.*;
import com.fschoen.parlorplace.backend.repository.*;
import com.fschoen.parlorplace.backend.service.*;
import com.fschoen.parlorplace.backend.utility.messaging.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

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
