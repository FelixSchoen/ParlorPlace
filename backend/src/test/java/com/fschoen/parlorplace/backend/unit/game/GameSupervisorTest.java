package com.fschoen.parlorplace.backend.unit.game;

import com.fschoen.parlorplace.backend.entity.transience.GameSupervisor;
import com.fschoen.parlorplace.backend.unit.base.BaseUnitTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GameSupervisorTest extends BaseUnitTest {

    @Test
    public void generateValidGameIdentifier_resultsInGameIdentifier() {
        GameSupervisor gameSupervisor = GameSupervisor.getInstance();

        assertThat(gameSupervisor.generateValidGameIdentifier()).isNotNull();
    }

}
