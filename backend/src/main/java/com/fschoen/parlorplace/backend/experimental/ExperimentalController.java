package com.fschoen.parlorplace.backend.experimental;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSignupRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RestController
public class ExperimentalController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/exp/send/{username}")
    public ResponseEntity<String> sendMessage(@PathVariable("username") String username, @RequestBody String message) {
        try {
            simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.OK).body("Done");
    }

}
