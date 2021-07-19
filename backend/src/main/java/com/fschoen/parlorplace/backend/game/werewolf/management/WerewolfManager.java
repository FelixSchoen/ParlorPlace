package com.fschoen.parlorplace.backend.game.werewolf.management;

import com.fschoen.parlorplace.backend.game.management.GameManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class WerewolfManager extends GameManager {

    @Autowired
    public WerewolfManager(WerewolfModerator werewolfModerator) {
        super(werewolfModerator);
    }

}
