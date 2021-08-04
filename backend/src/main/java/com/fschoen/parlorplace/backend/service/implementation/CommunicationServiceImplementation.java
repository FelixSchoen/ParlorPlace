package com.fschoen.parlorplace.backend.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.NotificationType;
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

    private static final String PRIMARY_DESTINATION_URI = "/queue/game/primary/";
    private static final String SECONDARY_DESTINATION_URI = "/queue/game/secondary/";
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public CommunicationServiceImplementation(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGameStaleNotification(GameIdentifier gameIdentifier, Set<User> recipients) {
        ClientNotification notification = ClientNotification.builder().notificationType(NotificationType.STALE_GAME_INFORMATION).build();
        for (User user : recipients) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                messagingTemplate.convertAndSendToUser(user.getUsername(), PRIMARY_DESTINATION_URI + gameIdentifier.getToken(), objectMapper.writeValueAsString(notification));
                log.info("Sent Game Stale Notification to User {} at {}", user.getUsername(), PRIMARY_DESTINATION_URI + gameIdentifier.getToken());
            } catch (MessagingException | JsonProcessingException e) {
                log.error("Could not send Game Stale Notification for User {}", user.getUsername());
            }
        }
    }

}
