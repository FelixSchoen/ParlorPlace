package com.fschoen.parlorplace.backend.integration.service;

import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.NotificationType;
import com.fschoen.parlorplace.backend.integration.base.BaseIntegrationTest;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.utility.communication.ClientNotification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void sendMessage_ToSpecificUsers_ResultsInUsersReceiveMessages() {
        User user1 = this.generatedData.getUserCollection().getUser1();
        User user2 = this.generatedData.getUserCollection().getUser2();
        GameIdentifier gameIdentifier = new GameIdentifier("TEST");

        StompSession sessionUser1 = connectNotification(user1, gameIdentifier);
        StompSession sessionUser2 = connectNotification(user2, gameIdentifier);

        this.sut.sendGameStaleNotification(gameIdentifier, new HashSet<>() {{
            add(user1);
            add(user2);
        }});

        ClientNotification notificationUser1 = waitNotification(user1, sessionUser1, gameIdentifier);
        ClientNotification notificationUser2 = waitNotification(user2, sessionUser2, gameIdentifier);

        assertThat(notificationUser1.getNotificationType()).isEqualTo(NotificationType.STALE_GAME_INFORMATION);
        assertThat(notificationUser2.getNotificationType()).isEqualTo(NotificationType.STALE_GAME_INFORMATION);

        this.closeSocket(sessionUser1);
        this.closeSocket(sessionUser2);
    }

}
