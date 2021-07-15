package com.fschoen.parlorplace.backend.experimental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ExperimentalController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/wss/exp")
    @SendTo("/wss/queue")
    public String processMessageFromClient(Principal principal, @Payload String message) {
        return "This is the only answer you are going to get " + message;
    }

    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        messagingTemplate.convertAndSend("/errors", exception.getMessage());
        return exception.getMessage();
    }

}
