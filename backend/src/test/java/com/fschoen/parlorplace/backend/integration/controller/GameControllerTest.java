package com.fschoen.parlorplace.backend.integration.controller;

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
    public void myTest() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();

        Response response = post(gameStartRequestDTO, GAME_BASE_URI + "start", getToken(existingUser));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
