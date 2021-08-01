package com.fschoen.parlorplace.backend.security;

import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;

@Service
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final AuthTokenFilter authTokenFilter;

    @Autowired
    public AuthChannelInterceptor(AuthTokenFilter authTokenFilter) {
        this.authTokenFilter = authTokenFilter;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authentication");
            Authentication authentication = authTokenFilter.authenticate(authHeader);

            if (authentication != null) {
                accessor.setUser(authentication);
            }
        }

        return message;
    }

}
