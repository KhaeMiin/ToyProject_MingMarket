package project.toyproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;


/**
 * websocket 설정
 * @EnableWebSocket: WebSocket 활성화
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Client에서 websocket 연결할 때 사용할 API 경로를 설정해준다.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*").withSockJS();
    }

    /**
     * enableSimpleBroker(): 메시지를 받을 때 관련 경로 설정
     * setApplicationDestinationPrefixes(): 메시지를 보낼 때 관련 경로 설정
     * 클라이언트가 메시지를 보낼 때 경로 맨 앞에 "/app"이 붙어있으면 Broker로 보내짐
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");

        registry.setApplicationDestinationPrefixes("/app");
    }
}
