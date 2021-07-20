package com.fschoen.parlorplace.backend.integration.controller;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.GameStartRequestDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfGame;
import com.fschoen.parlorplace.backend.integration.base.BaseIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameControllerTest extends BaseIntegrationTest {

    @Test
    public void startNewGame_resultsInNewGameCreated() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();

        Response response = post(gameStartRequestDTO, GAME_BASE_URI + "start", getToken(existingUser));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        GameDTO game = response.getBody().as(WerewolfGameDTO.class);
        assertThat(game.getGameIdentifier().getToken().length()).isGreaterThan(0);
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

        GameDTO game1 = response1.getBody().as(WerewolfGameDTO.class);
        GameDTO game2 = response2.getBody().as(WerewolfGameDTO.class);
        GameDTO game3 = response3.getBody().as(WerewolfGameDTO.class);

        assertThat(game1.getGameIdentifier()).isNotEqualTo(game2.getGameIdentifier());
        assertThat(game1.getGameIdentifier()).isNotEqualTo(game3.getGameIdentifier());
        assertThat(game2.getGameIdentifier()).isNotEqualTo(game3.getGameIdentifier());
    }

    @Test
    public void joinGame_withInvalidIdentifier_resultsInDataConflictException() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        String identifier = "NOTEXIST";

        Response response = payload("", getToken(existingUser)).pathParam("identifier", identifier).post(GAME_BASE_URI + "join/{identifier}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void joinGame_withUserAlreadyJoined_resultsInGameException() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();

        Response response1 = post(gameStartRequestDTO, GAME_BASE_URI + "start", getToken(existingUser));
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        GameDTO game1 = response1.getBody().as(WerewolfGameDTO.class);

        Response response2 = payload("", getToken(existingUser)).pathParam("identifier", game1.getGameIdentifier().getToken()).post(GAME_BASE_URI + "join/{identifier}").then().extract().response();
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void joinExistingGame_withValidIdentifier_resultsInGameJoined() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();

        Response response1 = post(gameStartRequestDTO, GAME_BASE_URI + "start", getToken(existingUser1));
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        GameDTO game1 = response1.getBody().as(WerewolfGameDTO.class);

        Response response2 = payload("", getToken(existingUser2)).pathParam("identifier", game1.getGameIdentifier().getToken()).post(GAME_BASE_URI + "join/{identifier}").then().extract().response();
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
        GameDTO game2 = response2.getBody().as(WerewolfGameDTO.class);
        assertThat(game2.getGameIdentifier()).isEqualTo(game1.getGameIdentifier());
    }

    private WerewolfGameDTO withWerewolfGame(User initiator, User... participants) {
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();
        Response responseStart = post(gameStartRequestDTO, GAME_BASE_URI + "start", getToken(initiator));
        WerewolfGameDTO gameDTO = responseStart.getBody().as(WerewolfGameDTO.class);

        for (User participant : participants) {
            payload("", getToken(participant)).pathParam("identifier", gameDTO.getGameIdentifier().getToken()).post(GAME_BASE_URI + "join/{identifier}").then().extract().response();
        }

        return gameDTO;
    }

}
