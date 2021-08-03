package com.fschoen.parlorplace.backend.integration.controller;

import com.fschoen.parlorplace.backend.controller.dto.game.GameBaseInformationDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.integration.base.BaseIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneralGameControllerTest extends BaseIntegrationTest {

    @Test
    public void obtainBaseInformationOfExistingGame_resultsInValidBaseInformation() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        WerewolfGameDTO werewolfGameDTO = withWerewolfGame(existingUser);
        assertThat(werewolfGameDTO.getGameIdentifier()).isNotNull();

        Response response = payload("", getToken(existingUser)).pathParam("identifier", werewolfGameDTO.getGameIdentifier().getToken())
                .get(GENERAL_BASE_URI + "base_info/{identifier}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        GameBaseInformationDTO gameBaseInformationDTO = response.getBody().as(GameBaseInformationDTO.class);
        assertThat(gameBaseInformationDTO.getGameIdentifier()).isEqualTo(werewolfGameDTO.getGameIdentifier());
        assertThat(gameBaseInformationDTO.getGameType()).isEqualTo(werewolfGameDTO.getGameType());
        assertThat(gameBaseInformationDTO.getGameState()).isEqualTo(werewolfGameDTO.getGameState());
    }

    @Test
    public void obtainBaseInformationOfExistingGame_withPlayerNotPartOfGame_resultsInValidBaseInformation() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();
        WerewolfGameDTO werewolfGameDTO = withWerewolfGame(existingUser1);
        assertThat(werewolfGameDTO.getGameIdentifier()).isNotNull();

        Response response = payload("", getToken(existingUser2)).pathParam("identifier", werewolfGameDTO.getGameIdentifier().getToken())
                .get(GENERAL_BASE_URI + "base_info/{identifier}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
