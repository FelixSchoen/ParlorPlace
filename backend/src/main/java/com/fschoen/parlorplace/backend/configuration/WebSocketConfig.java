package com.fschoen.parlorplace.backend.configuration;

import com.fschoen.parlorplace.backend.security.AuthTokenFilter;
import com.fschoen.parlorplace.backend.security.JwtUtils;
import com.fschoen.parlorplace.backend.service.implementation.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    protected final UserDetailsServiceImplementation userDetailsService;
    protected final JwtUtils jwtUtils;
    private final AuthTokenFilter authTokenFilter;

    @Autowired
    public WebSocketConfig(UserDetailsServiceImplementation userDetailsService, JwtUtils jwtUtils, AuthTokenFilter authTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.authTokenFilter = authTokenFilter;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/", "/queue/");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/greeting")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        {
            registration.interceptors(new ChannelInterceptor() {

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
            });
        }
    }

}
