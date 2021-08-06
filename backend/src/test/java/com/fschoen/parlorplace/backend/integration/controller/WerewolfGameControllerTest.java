package com.fschoen.parlorplace.backend.integration.controller;

import com.fschoen.parlorplace.backend.controller.dto.game.GameStartRequestDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.enumeration.LobbyRole;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfLobbyChangeRequestDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfRuleSetDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import com.fschoen.parlorplace.backend.integration.base.BaseIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class WerewolfGameControllerTest extends BaseIntegrationTest {

    @Test
    public void startNewGame_resultsInNewGameCreated() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();

        Response response = post(gameStartRequestDTO, WEREWOLF_BASE_URI + "host", getToken(existingUser));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        WerewolfGameDTO game = response.getBody().as(WerewolfGameDTO.class);
        assertThat(game.getGameIdentifier().getToken().length()).isGreaterThan(0);
    }

    @Test
    public void startMultipleNewGames_resultsInNewGameCreated_withDistinctIdentifiers() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();
        User existingUser3 = this.generatedData.getUserCollection().getUser3();

        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();

        Response response1 = post(gameStartRequestDTO, WEREWOLF_BASE_URI + "host", getToken(existingUser1));
        Response response2 = post(gameStartRequestDTO, WEREWOLF_BASE_URI + "host", getToken(existingUser2));
        Response response3 = post(gameStartRequestDTO, WEREWOLF_BASE_URI + "host", getToken(existingUser3));

        WerewolfGameDTO game1 = response1.getBody().as(WerewolfGameDTO.class);
        WerewolfGameDTO game2 = response2.getBody().as(WerewolfGameDTO.class);
        WerewolfGameDTO game3 = response3.getBody().as(WerewolfGameDTO.class);

        assertThat(game1.getGameIdentifier()).isNotEqualTo(game2.getGameIdentifier());
        assertThat(game1.getGameIdentifier()).isNotEqualTo(game3.getGameIdentifier());
        assertThat(game2.getGameIdentifier()).isNotEqualTo(game3.getGameIdentifier());
    }

    @Test
    public void joinGame_withInvalidIdentifier_resultsInDataConflictException() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        String identifier = "NOT";

        Response response = payload("", getToken(existingUser)).pathParam("identifier", identifier).post(WEREWOLF_BASE_URI + "join/{identifier}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void joinGame_withUserAlreadyJoined_resultsInGameException() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();

        Response response1 = post(gameStartRequestDTO, WEREWOLF_BASE_URI + "host", getToken(existingUser));
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        WerewolfGameDTO game1 = response1.getBody().as(WerewolfGameDTO.class);

        Response response2 = payload("", getToken(existingUser)).pathParam("identifier", game1.getGameIdentifier().getToken()).post(WEREWOLF_BASE_URI + "join/{identifier}").then().extract().response();
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }


    @Test
    public void joinExistingGame_withValidIdentifier_resultsInGameJoined() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();

        Response response1 = post(gameStartRequestDTO, WEREWOLF_BASE_URI + "host", getToken(existingUser1));
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        WerewolfGameDTO game1 = response1.getBody().as(WerewolfGameDTO.class);

        Response response2 = payload("", getToken(existingUser2)).pathParam("identifier", game1.getGameIdentifier().getToken())
                .post(WEREWOLF_BASE_URI + "join/{identifier}").then().extract().response();
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
        WerewolfGameDTO game2 = response2.getBody().as(WerewolfGameDTO.class);
        assertThat(game2.getGameIdentifier()).isEqualTo(game1.getGameIdentifier());
    }

    @Test
    public void changeLobbyOptions_withValidData_resultsInLobbyChanged() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();

        WerewolfGameDTO werewolfGameDTO = withWerewolfGame(existingUser1, existingUser2);
        Set<WerewolfPlayerDTO> werewolfPlayerDTOS = werewolfGameDTO.getPlayers();
        werewolfPlayerDTOS.forEach(playerDTO -> playerDTO.setPosition((playerDTO.getPosition() + 1) % 2));

        WerewolfLobbyChangeRequestDTO werewolfLobbyChangeRequestDTO = WerewolfLobbyChangeRequestDTO.builder().players(werewolfPlayerDTOS).ruleSet(werewolfGameDTO.getRuleSet()).build();

        Response response = payload(werewolfLobbyChangeRequestDTO, getToken(existingUser1)).pathParam("identifier", werewolfGameDTO.getGameIdentifier().getToken())
                .post(WEREWOLF_BASE_URI + "lobby/change/{identifier}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        werewolfGameDTO = response.getBody().as(WerewolfGameDTO.class);
        assertThat(werewolfGameDTO.getPlayers().stream()
                .filter(playerDTO -> playerDTO.getUser().getId().equals(existingUser1.getId())).findFirst().orElseThrow().getPosition()).isEqualTo(1);
        assertThat(werewolfGameDTO.getPlayers().stream()
                .filter(playerDTO -> playerDTO.getUser().getId().equals(existingUser2.getId())).findFirst().orElseThrow().getPosition()).isEqualTo(0);
    }

    @Test
    public void changeLobbyOptions_withInvalidLobby_resultsInDataConflictException() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();

        WerewolfGameDTO werewolfGameDTO = withWerewolfGame(existingUser1, existingUser2);
        Set<WerewolfPlayerDTO> werewolfPlayerDTOS = werewolfGameDTO.getPlayers();

        WerewolfLobbyChangeRequestDTO werewolfLobbyChangeRequestDTO = WerewolfLobbyChangeRequestDTO.builder().players(werewolfPlayerDTOS).ruleSet(werewolfGameDTO.getRuleSet()).build();

        Response response = payload(werewolfLobbyChangeRequestDTO, getToken(existingUser1)).pathParam("identifier", "NOT")
                .post(WEREWOLF_BASE_URI + "lobby/change/{identifier}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void changeLobbyOptions_withInvalidData_resultsInDataConflictException() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();

        WerewolfGameDTO werewolfGameDTO = withWerewolfGame(existingUser1, existingUser2);
        Set<WerewolfPlayerDTO> werewolfPlayerDTOS = werewolfGameDTO.getPlayers();
        werewolfPlayerDTOS.forEach(playerDTO -> playerDTO.setPosition(playerDTO.getPosition() + 1));

        WerewolfLobbyChangeRequestDTO werewolfLobbyChangeRequestDTO = WerewolfLobbyChangeRequestDTO.builder().players(werewolfPlayerDTOS).ruleSet(werewolfGameDTO.getRuleSet()).build();

        Response response = payload(werewolfLobbyChangeRequestDTO, getToken(existingUser1)).pathParam("identifier", werewolfGameDTO.getGameIdentifier().getToken())
                .post(WEREWOLF_BASE_URI + "lobby/change/{identifier}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void addRolesToWerewolfLobby_resultsInRolesAdded() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();

        WerewolfGameDTO werewolfGameDTO = withWerewolfGame(existingUser1, existingUser2);
        WerewolfRuleSetDTO werewolfRuleSetDTO = werewolfGameDTO.getRuleSet();
        werewolfRuleSetDTO.getGameRoleTypes().add(WerewolfRoleType.WEREWOLF);
        werewolfRuleSetDTO.getGameRoleTypes().add(WerewolfRoleType.VILLAGER);

        WerewolfLobbyChangeRequestDTO werewolfLobbyChangeRequestDTO = WerewolfLobbyChangeRequestDTO.builder().players(werewolfGameDTO.getPlayers()).ruleSet(werewolfRuleSetDTO).build();

        Response response = payload(werewolfLobbyChangeRequestDTO, getToken(existingUser1)).pathParam("identifier", werewolfGameDTO.getGameIdentifier().getToken())
                .post(WEREWOLF_BASE_URI + "lobby/change/{identifier}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        werewolfGameDTO = response.getBody().as(WerewolfGameDTO.class);
        assertThat(werewolfGameDTO.getRuleSet().getGameRoleTypes()).contains(WerewolfRoleType.WEREWOLF);
        assertThat(werewolfGameDTO.getRuleSet().getGameRoleTypes()).contains(WerewolfRoleType.VILLAGER);
    }

    @Test
    public void getActiveGame_withValidIdentifier_resultsInGameReturned() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();

        WerewolfGameDTO werewolfGameDTO = withWerewolfGame(existingUser1, existingUser2);

        Response response = payload("", getToken(existingUser1)).pathParam("identifier", werewolfGameDTO.getGameIdentifier().getToken())
                .get(WEREWOLF_BASE_URI + "{identifier}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        WerewolfGameDTO gameDTOResponse = response.getBody().as(WerewolfGameDTO.class);
        assertThat(gameDTOResponse).isEqualTo(werewolfGameDTO);
    }

    @Test
    public void getGame_withInvalidIdentifier_resultsInDataConflictException() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();

        Response response = payload("", getToken(existingUser1)).pathParam("identifier", "NOT")
                .get(WEREWOLF_BASE_URI + "{identifier}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void quitExistingGame_resultsInGameRemoved() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();
        Response responseCreate = post(gameStartRequestDTO, WEREWOLF_BASE_URI + "host", getToken(existingUser));
        WerewolfGameDTO game = responseCreate.getBody().as(WerewolfGameDTO.class);

        Response responseQuit = payload("", getToken(existingUser)).pathParam("identifier", game.getGameIdentifier().getToken())
                .post(WEREWOLF_BASE_URI + "quit/{identifier}").then().extract().response();
        assertThat(responseQuit.statusCode()).isEqualTo(HttpStatus.OK.value());

        Response responseRejoin = payload("", getToken(existingUser)).pathParam("identifier", game.getGameIdentifier().getToken())
                .post(WEREWOLF_BASE_URI + "join/{identifier}").then().extract().response();
        assertThat(responseRejoin.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void quitNonExistingGame_resultsInDataConflictException() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        Response responseQuit = payload("", getToken(existingUser)).pathParam("identifier", "NOT")
                .post(WEREWOLF_BASE_URI + "quit/{identifier}").then().extract().response();
        assertThat(responseQuit.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void quitGame_notPartOf_resultsInGameException() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();
        Response responseCreate = post(gameStartRequestDTO, WEREWOLF_BASE_URI + "host", getToken(existingUser1));
        WerewolfGameDTO game = responseCreate.getBody().as(WerewolfGameDTO.class);

        Response responseQuit = payload("", getToken(existingUser2)).pathParam("identifier", game.getGameIdentifier().getToken())
                .post(WEREWOLF_BASE_URI + "quit/{identifier}").then().extract().response();
        assertThat(responseQuit.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void quitGameWithTwoPlayers_removesPlayer_otherPlayerIsNewHost() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        User existingUser2 = this.generatedData.getUserCollection().getUser2();
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();
        Response responseCreate = post(gameStartRequestDTO, WEREWOLF_BASE_URI + "host", getToken(existingUser1));
        WerewolfGameDTO game = responseCreate.getBody().as(WerewolfGameDTO.class);

        Response responseJoin = payload("", getToken(existingUser2)).pathParam("identifier", game.getGameIdentifier().getToken())
                .post(WEREWOLF_BASE_URI + "join/{identifier}").then().extract().response();
        assertThat(responseJoin.statusCode()).isEqualTo(HttpStatus.OK.value());

        Response responseQuit = payload("", getToken(existingUser1)).pathParam("identifier", game.getGameIdentifier().getToken())
                .post(WEREWOLF_BASE_URI + "quit/{identifier}").then().extract().response();
        assertThat(responseQuit.statusCode()).isEqualTo(HttpStatus.OK.value());

        Response responseGet = payload("", getToken(existingUser2)).pathParam("identifier", game.getGameIdentifier().getToken())
                .get(WEREWOLF_BASE_URI + "{identifier}").then().extract().response();
        assertThat(responseGet.statusCode()).isEqualTo(HttpStatus.OK.value());

        WerewolfGameDTO gameAfterQuit = responseGet.getBody().as(WerewolfGameDTO.class);
        assertThat(gameAfterQuit.getPlayers().size()).isEqualTo(1);
        assertThat(gameAfterQuit.getPlayers().stream().findFirst().orElseThrow().getUser().getId()).isEqualTo(existingUser2.getId());
        assertThat(gameAfterQuit.getPlayers().stream().findFirst().orElseThrow().getLobbyRole()).isEqualTo(LobbyRole.ROLE_ADMIN);
    }

    @Test
    public void getActiveGamesOfUser() {
        User existingUser1 = this.generatedData.getUserCollection().getUser1();
        WerewolfGameDTO werewolfGameDTO = withWerewolfGame(existingUser1);

        Response responseGetActiveGames = get("", WEREWOLF_BASE_URI + "active", getToken(existingUser1));
        assertThat(responseGetActiveGames.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<WerewolfGameDTO> werewolfGameDTOList = Arrays.asList(responseGetActiveGames.getBody().as(WerewolfGameDTO[].class));
        assertThat(werewolfGameDTOList).anyMatch(gameDTO -> gameDTO.getGameIdentifier().equals(werewolfGameDTO.getGameIdentifier()));
    }

    @Test
    public void startLobbyGame() {
        User admin1 = this.generatedData.getUserCollection().getAdmin1();
        WerewolfGame werewolfGame = this.generatedData.getWerewolfGameCollection().getWerewolfGame1();

        Response responseStartGame = post("", WEREWOLF_BASE_URI + "start/" + werewolfGame.getGameIdentifier().getToken(), getToken(admin1));
        System.out.println(responseStartGame.getBody().peek());
        assertThat(responseStartGame.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
