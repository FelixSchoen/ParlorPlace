package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.GameRole;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.RuleSet;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class AbstractGameModerator<
        G extends Game<P, RS>,
        P extends Player<GR>,
        RS extends RuleSet,
        GR extends GameRole,
        GRepo extends GameRepository<G>
        > implements Runnable {

    protected GameIdentifier gameIdentifier;

}
