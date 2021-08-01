package com.fschoen.parlorplace.backend.configuration;

import org.springframework.context.annotation.*;
import org.springframework.messaging.simp.*;
import org.springframework.security.config.annotation.web.messaging.*;
import org.springframework.security.config.annotation.web.socket.*;

@Configuration
public class SecurityWebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.DISCONNECT, SimpMessageType.OTHER).permitAll()
                .anyMessage().authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

}
