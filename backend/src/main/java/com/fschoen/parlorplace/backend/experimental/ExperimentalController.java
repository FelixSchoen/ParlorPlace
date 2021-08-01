package com.fschoen.parlorplace.backend.experimental;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

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
