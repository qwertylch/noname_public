package com.noname.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.noname.security.CustomUser;

@Slf4j
@Controller
//@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    // pub/chat/{productId}
    @MessageMapping("/chat/{productId}") // 클라이언트로 부터 send 요청을 들어오는 로직 처리
    // chat/{productId}에서 @DestinationVariable 로 productId 값을 문자열로 가져오고
    //@SessionAttribute(name = "sid", required = false) Long sid,
    public void sendMessage(@DestinationVariable Long productId , String message) {
//    	@AuthenticationPrincipal CustomUser user user.getMember().getMemberId();
    	
        // 서버가 클라이언트에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/"+productId, message);
        log.info("서버에서 클라이언트로 보내는 message : {} ", message);
    }
}
