package com.fschoen.parlorplace.backend.experimental;

import com.fschoen.parlorplace.backend.enumeration.NotificationType;
import com.fschoen.parlorplace.backend.utility.communication.ClientNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class ExperimentalController {

    private static final String DESTINATION_URI = "/queue/game/";
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/exp/send/{username}")
    public ResponseEntity<String> sendMessage(@PathVariable("username") String username, @RequestBody String message) {
        try {
            messagingTemplate.convertAndSendToUser(username, DESTINATION_URI + "TEST", ClientNotification.builder().notificationType(NotificationType.STALE_GAME_INFORMATION).build());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.OK).body("Done");
    }

}
