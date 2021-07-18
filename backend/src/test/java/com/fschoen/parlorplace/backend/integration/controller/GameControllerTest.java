package com.fschoen.parlorplace.backend.integration.controller;

import com.fschoen.parlorplace.backend.controller.dto.game.GameIdentifierDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.GameStartRequestDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.integration.base.BaseIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class GameControllerTest extends BaseIntegrationTest {

    @Test
    public void startNewGame_resultsInNewGameCreated() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();

        Response response = post(gameStartRequestDTO, GAME_BASE_URI + "start", getToken(existingUser));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        GameIdentifierDTO gameIdentifierDTO = response.getBody().as(GameIdentifierDTO.class);
        assertThat(gameIdentifierDTO.getToken().length()).isGreaterThan(0);
    }

    @Test
    public void startMultipleNewGames_resultsInNewGameCreated_withDistinctIdentifiers() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();
        User existingUser3 = this.generatedData.getUserCollection().getUser3();

        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();

        Response response1 = post(gameStartRequestDTO, GAME_BASE_URI + "start", getToken(existingUser1));
        Response response2 = post(gameStartRequestDTO, GAME_BASE_URI + "start", getToken(existingUser2));
        Response response3 = post(gameStartRequestDTO, GAME_BASE_URI + "start", getToken(existingUser3));

        GameIdentifierDTO gameIdentifierDTO1 = response1.getBody().as(GameIdentifierDTO.class);
        GameIdentifierDTO gameIdentifierDTO2 = response2.getBody().as(GameIdentifierDTO.class);
        GameIdentifierDTO gameIdentifierDTO3 = response3.getBody().as(GameIdentifierDTO.class);

        assertThat(gameIdentifierDTO1).isNotEqualTo(gameIdentifierDTO2);
        assertThat(gameIdentifierDTO1).isNotEqualTo(gameIdentifierDTO3);
        assertThat(gameIdentifierDTO2).isNotEqualTo(gameIdentifierDTO3);
    }

}
