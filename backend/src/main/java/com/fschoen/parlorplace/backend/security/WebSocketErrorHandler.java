package com.fschoen.parlorplace.backend.security;

import lombok.extern.slf4j.*;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.*;
import org.springframework.web.socket.messaging.*;

import java.nio.charset.*;
import java.nio.file.*;

@Slf4j
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        log.error("Unauthorized error: {}", ex.getMessage());

        if (ex instanceof MessageDeliveryException) {
            return prepareErrorMessage("Error while delivering message");
        }

        if (ex instanceof IllegalStateException) {
            return prepareErrorMessage("Error no decoder available");
        }

        if (ex instanceof AccessDeniedException) {
            return prepareErrorMessage("Error access denied");
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> prepareErrorMessage(String message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }


}
