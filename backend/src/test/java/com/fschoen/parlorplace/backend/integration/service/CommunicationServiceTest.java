package com.fschoen.parlorplace.backend.integration.service;

import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.StaleType;
import com.fschoen.parlorplace.backend.integration.base.BaseIntegrationTest;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.utility.communication.ClientNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;

import static org.assertj.core.api.Assertions.assertThat;

public class CommunicationServiceTest extends BaseIntegrationTest {

    @Autowired
    private CommunicationService sut;

    @Test
    public void sendMessagesToNotExistingUsers() {
        User user = this.generatedData.getUserCollection().getUser1();
        this.sut.sendGameStaleNotification(new GameIdentifier("TEST"), new HashSet<>(){{
            add(user);
        }});
    }

    @Test
    public void sendMessageCanBeReceived() {
        User user = this.generatedData.getUserCollection().getUser1();

        StompSession stompSession = connectSocket(user, new GameIdentifier("TEST"));

        this.sut.sendGameStaleNotification(new GameIdentifier("TEST"), new HashSet<>(){{
            add(user);
        }});

        ClientNotification clientNotification = readSocket(stompSession, new GameIdentifier("TEST"));
        System.out.println(clientNotification);
        assertThat(clientNotification.getStaleType()).isEqualTo(StaleType.GAME);
    }





}
