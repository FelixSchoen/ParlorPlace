package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.StaleType;
import com.fschoen.parlorplace.backend.service.CommunicationService;
import com.fschoen.parlorplace.backend.utility.communication.ClientNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class CommunicationServiceImplementation implements CommunicationService {

    private final SimpMessagingTemplate messagingTemplate;

    private static final String DESTINATION_URI = "/queue/game/";

    @Autowired
    public CommunicationServiceImplementation(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGameStaleNotification(GameIdentifier gameIdentifier, Set<User> recipients) {
        for (User user : recipients) {
            try {
                messagingTemplate.convertAndSendToUser(user.getUsername(), DESTINATION_URI + gameIdentifier.getToken(), ClientNotification.builder().staleType(StaleType.GAME).build());
            } catch (MessagingException e) {
                log.error("Could not send Game Stale Notification for User {}", user.getUsername());
            }
        }
    }

}
