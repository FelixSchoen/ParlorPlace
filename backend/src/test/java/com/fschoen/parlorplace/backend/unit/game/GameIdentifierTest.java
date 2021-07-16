package com.fschoen.parlorplace.backend.unit.game;

import com.fschoen.parlorplace.backend.entity.transience.GameIdentifier;
import com.fschoen.parlorplace.backend.unit.base.BaseUnitTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GameIdentifierTest extends BaseUnitTest {

    @Test
    public void generateGameIdentifierOfLength4_resultsInGameIdentifierOfLength4() {
        GameIdentifier sut = new GameIdentifier(4);

        assertThat(sut.getToken().length()).isEqualTo(4);
    }

}
