package com.fschoen.parlorplace.backend.integration.service;

import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.integration.base.BaseIntegrationTest;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.utility.communication.ClientNotification;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.HashSet;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommunicationServiceTest extends BaseIntegrationTest {

    @Autowired
    private CommunicationService sut;

    @Test
    public void A() {
        User user = this.generatedData.getUserCollection().getUser1();
        this.sut.sendGameStaleNotification(new GameIdentifier("TEST"), new HashSet<>(){{
            add(user);
        }});
    }

    @Test
    public void B() {
        User user = this.generatedData.getUserCollection().getUser1();

        StompSession stompSession = connectSocket(user, new GameIdentifier("TEST"));

        this.sut.sendGameStaleNotification(new GameIdentifier("TEST"), new HashSet<>(){{
            add(user);
        }});

        ClientNotification clientNotification = readSocket(stompSession, new GameIdentifier("TEST"));
        // TODO Fix JSON Parsing - See BaseIntegrationTest#handleFrame
        //assertThat(clientNotification.getNotificationType()).isEqualTo(NotificationType.STALE_GAME_INFORMATION);
    }





}
