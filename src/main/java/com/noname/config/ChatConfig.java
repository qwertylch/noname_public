package com.noname.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

    // 메시지 처리 요청 (내장브로커, 컨트롤러)
    @Override
    public void configureMessageBroker(final MessageBrokerRegistry regist) { // 내장 브로커를 사용하겠다. ( 추후에 외부브로커 사용 )
        /*
            sub -> 바로 내장 브로커에 토스
            pub -> handler로 처리후 거기에서 브로커에 토스
                : 핸들러를 통해서 부가적인 처리가 필요한 경우 사용
         */
        regist.enableSimpleBroker("/sub"); // 내장 브로커에 바로 전달
        regist.setApplicationDestinationPrefixes("/pub"); // 컨트롤러 (=STOMPController)에 전달 후 처리
        //http://localhost:8080/product/chat 로 호출되면 작동 하도록 설정
    }


    // endPoint 설정 ("/ws")
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        // 서버와 연결(핸드셰이크)될 endpoint 설정
        // ws://localhost:8080/chat -> 서버 연결
        registry.addEndpoint("/chat")
                .setAllowedOrigins("http://localhost:8080")
                .withSockJS(); // SockJS 추가
    }
}
