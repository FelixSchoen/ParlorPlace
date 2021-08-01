package com.fschoen.parlorplace.backend.game.werewolf.management;

import com.fschoen.parlorplace.backend.game.management.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Component
@Scope("prototype")
public class WerewolfManager extends GameManager {

    @Autowired
    public WerewolfManager(WerewolfModerator werewolfModerator) {
        super(werewolfModerator);
    }

}
