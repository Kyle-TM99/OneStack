package com.onestack.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    // 메시지 브로커 설정
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지를 구독하는 prefix
        config.enableSimpleBroker("/topic", "/queue");
        // 메시지를 발행하는 prefix
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    // STOMP 엔드포인트 등록
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")  // 모든 출처 허용 (개발용)
                .withSockJS();
    }
} 