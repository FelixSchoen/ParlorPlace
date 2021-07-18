package com.fschoen.parlorplace.backend.game.werewolf.management;

import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.entity.transience.GameState;
import com.fschoen.parlorplace.backend.game.management.GameInstance;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import com.fschoen.parlorplace.backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class WerewolfInstance extends GameInstance {

    @Autowired
    public WerewolfInstance(GameService gameService, WerewolfManager werewolfManager) {
        super(gameService, werewolfManager);
    }

    public GameState join(User user) {
        WerewolfPlayer werewolfPlayer = WerewolfPlayer.builder().user(user).build();

        return null;
    }

}
