package com.fschoen.parlorplace.backend.integration.service;

import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.NotificationType;
import com.fschoen.parlorplace.backend.integration.base.BaseIntegrationTest;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.utility.communication.ClientNotification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
public class CommunicationServiceTest extends BaseIntegrationTest {

    @Autowired
    private CommunicationService sut;

    @Test
    public void sendMessage_ToNonConnectedUsers_ResultsInNoException() {
        User user = this.generatedData.getUserCollection().getUser1();
        this.sut.sendGameStaleNotification(new GameIdentifier("TEST"), new HashSet<>() {{
            add(user);
        }});
    }

    @RepeatedTest(2)
    public void sendMessage_ToSpecificUser_ResultsInUserReceivesMessage() {
        User user = this.generatedData.getUserCollection().getUser1();

        StompSession stompSession = connectSocket(user, new GameIdentifier("TEST"));

        this.sut.sendGameStaleNotification(new GameIdentifier("TEST"), new HashSet<>() {{
            add(user);
        }});

        ClientNotification clientNotification = readSocket(stompSession, new GameIdentifier("TEST"));
        assertThat(clientNotification.getNotificationType()).isEqualTo(NotificationType.STALE_GAME_INFORMATION);

        this.closeSocket(stompSession);
    }

}
