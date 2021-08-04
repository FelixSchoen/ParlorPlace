package com.fschoen.parlorplace.backend.integration.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fschoen.parlorplace.backend.ParlorPlaceApplication;
import com.fschoen.parlorplace.backend.controller.dto.game.GameStartRequestDTO;
import com.fschoen.parlorplace.backend.datagenerator.DatabasePopulator;
import com.fschoen.parlorplace.backend.datagenerator.GeneratedData;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.integration.utility.TestIsolationService;
import com.fschoen.parlorplace.backend.security.JwtUtils;
import com.fschoen.parlorplace.backend.security.UserDetailsImplementation;
import com.fschoen.parlorplace.backend.utility.communication.ClientNotification;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

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

    protected static final String BASE_URI = "/";
    protected static final String USER_BASE_URI = BASE_URI + "user/";
    protected static final String GAME_BASE_URI = BASE_URI + "game/";
    protected static final String GENERAL_BASE_URI = GAME_BASE_URI + "general/";
    protected static final String WEREWOLF_BASE_URI = GAME_BASE_URI + "werewolf/";
    protected static final String WEBSOCKET_QUEUE_PRIMARY_URI = "/user/queue/game/primary/";

    protected String WEBSOCKET_GAME_URI;
    protected CompletableFuture<Object> notificationFuture;

    @LocalServerPort
    protected int port;

    @BeforeEach
    public void beforeBase() {
        testIsolationService.recreateDatabase();
        this.generatedData = databasePopulator.generate();

        RestAssured.port = port;
        WEBSOCKET_GAME_URI = "ws://localhost:" + port + "/communication/game";
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

    // Websocket

    protected StompSession connectSocket(User user, GameIdentifier gameIdentifier) {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        //stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        webSocketHttpHeaders.add("Authorization", getToken(user));

        StompSessionHandler stompSessionHandler = new NotificationStompSessionHandler();

        StompSession stompSession = null;

        try {
            stompSession = stompClient.connect(WEBSOCKET_GAME_URI, webSocketHttpHeaders, stompSessionHandler).get(1, SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();
        }

        assert stompSession != null;
        subscribeSocket(stompSession, gameIdentifier);

        return stompSession;
    }

    private void subscribeSocket(StompSession stompSession, GameIdentifier gameIdentifier) {
        this.notificationFuture = new CompletableFuture<>();
        stompSession.subscribe(WEBSOCKET_QUEUE_PRIMARY_URI + gameIdentifier.getToken(), new NotificationStompSessionHandler());
    }

    protected ClientNotification readSocket(StompSession stompSession, GameIdentifier gameIdentifier) {
        try {
            ClientNotification clientNotification = (ClientNotification) this.notificationFuture.get(5, SECONDS);
            subscribeSocket(stompSession, gameIdentifier);

            return clientNotification;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void closeSocket(StompSession stompSession) {
        stompSession.disconnect();
    }

    @Slf4j
    private class NotificationStompSessionHandler extends StompSessionHandlerAdapter implements StompSessionHandler {

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.debug("Connected to socket");
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders
                headers, byte[] payload, Throwable exception) {
            log.error("Exception dealing with socket", exception);
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            log.debug("Received Message");
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            ObjectMapper objectMapper = new ObjectMapper();
            ClientNotification clientNotification = null;
            try {
                clientNotification = objectMapper.readValue(new String((byte[]) payload), ClientNotification.class);
            } catch (JsonProcessingException e) {
                log.error("Error deserializing payload", e);
            }
            notificationFuture.complete(clientNotification);
        }

    }

}
