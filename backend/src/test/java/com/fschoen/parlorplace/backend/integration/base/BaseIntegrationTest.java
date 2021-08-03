package com.fschoen.parlorplace.backend.integration.base;

import com.fschoen.parlorplace.backend.ParlorPlaceApplication;
import com.fschoen.parlorplace.backend.controller.dto.game.GameStartRequestDTO;
import com.fschoen.parlorplace.backend.datagenerator.DatabasePopulator;
import com.fschoen.parlorplace.backend.datagenerator.GeneratedData;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.integration.utility.TestIsolationService;
import com.fschoen.parlorplace.backend.security.JwtUtils;
import com.fschoen.parlorplace.backend.security.UserDetailsImplementation;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ParlorPlaceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public abstract class BaseIntegrationTest {

    protected GeneratedData generatedData;
    @Autowired
    private DatabasePopulator databasePopulator;
    @Autowired
    private TestIsolationService testIsolationService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    protected String BASE_URI = "/";
    protected String USER_BASE_URI = BASE_URI + "user/";
    protected String GAME_BASE_URI = BASE_URI + "game/";
    protected String GENERAL_BASE_URI = GAME_BASE_URI + "general/";
    protected String WEREWOLF_BASE_URI = GAME_BASE_URI + "werewolf/";

    @LocalServerPort
    protected int port;

    @BeforeEach
    public void beforeBase() {
        testIsolationService.recreateDatabase();
        this.generatedData = databasePopulator.generate();

        RestAssured.port = port;
    }

    // Authentication

    protected String getToken(User userInTestdata) {
        return getToken(userInTestdata.getUsername(), generatedData.getPasswordCollection().get(userInTestdata));
    }

    protected String getToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        String accessToken = jwtUtils.generateJwtToken(userDetails);
        return Strings.join("Bearer ", accessToken).with("");
    }

    // Games

    protected WerewolfGameDTO withWerewolfGame(User initiator, User... participants) {
        GameStartRequestDTO gameStartRequestDTO = GameStartRequestDTO.builder().gameType(GameType.WEREWOLF).build();
        Response responseStart = post(gameStartRequestDTO, WEREWOLF_BASE_URI + "host", getToken(initiator));
        WerewolfGameDTO gameDTO = responseStart.getBody().as(WerewolfGameDTO.class);

        for (User participant : participants) {
            Response responseJoin = payload("", getToken(participant)).pathParam("identifier", gameDTO.getGameIdentifier().getToken()).post(WEREWOLF_BASE_URI + "join/{identifier}").then().extract().response();
            gameDTO = responseJoin.getBody().as(WerewolfGameDTO.class);
        }

        return gameDTO;
    }

    // REST

    protected Response post(Object o, String URI) {
        return post(o, URI, "");
    }

    protected Response get(Object o, String URI, String authorization) {
        return payload(o, authorization)
                .get(URI)
                .then()
                .extract()
                .response();
    }

    protected Response post(Object o, String URI, String authorization) {
        return payload(o, authorization)
                .post(URI)
                .then()
                .extract()
                .response();
    }

    protected Response put(Object o, String URI, String authorization) {
        return payload(o, authorization)
                .put(URI)
                .then()
                .extract()
                .response();
    }

    protected Response patch(Object o, String URI, String authorization) {
        return payload(o, authorization)
                .patch(URI)
                .then()
                .extract()
                .response();
    }

    protected Response delete(Object o, String URI, String authorization) {
        return payload(o, authorization)
                .delete(URI)
                .then()
                .extract()
                .response();
    }

    protected RequestSpecification payload(Object o, String authorization) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .body(o);
    }

}
