package com.fschoen.parlorplace.backend.unit.game;

import com.fschoen.parlorplace.backend.game.management.*;
import com.fschoen.parlorplace.backend.unit.base.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class GameIdentifierTest extends BaseUnitTest {

    @Test
    public void generateGameIdentifierOfLength4_resultsInGameIdentifierOfLength4() {
        GameIdentifier sut = new GameIdentifier(4);

        assertThat(sut.getToken().length()).isEqualTo(4);
    }

}
