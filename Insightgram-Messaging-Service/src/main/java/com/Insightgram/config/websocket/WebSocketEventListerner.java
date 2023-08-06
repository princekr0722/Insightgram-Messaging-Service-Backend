package com.Insightgram.config.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.Insightgram.dto.ReceiveMessage;
import com.Insightgram.dto.ReceiveMessage.MessageType;

import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class WebSocketEventListerner {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@EventListener
	public void onStompConnect(SessionSubscribeEvent event) {
		
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage()); 
//		simpMessagingTemplate.convertAndSend(headerAccessor.getDestination(), new ChatMessage("Sender Name", "Subscribed", ChatMessage.MessageType.JOIN));
	}
	
	@EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        
//        simpMessagingTemplate.convertAndSend("/topic/public", chatMessage);
    }
}
