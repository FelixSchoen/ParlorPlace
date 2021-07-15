package com.fschoen.parlorplace.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;

public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketErrorHandler.class);

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        LOGGER.error("Unauthorized error: {}", ex.getMessage());

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
