package com.Insightgram.config.websocket;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.Insightgram.dto.ReceiveMessage;
import com.Insightgram.dto.ReceiveMessage.MessageType;
import com.Insightgram.entity.ChatTextMessage;
import com.Insightgram.service.ChatTextMessagesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SendMessageChannel {
	
	@Autowired
	private ChatTextMessagesService chatTextMessagesService;
	
	public Message<byte[]> getSendableMessage(final Message<?> message, StompHeaderAccessor accessor, Authentication authentication) {
		String destination = accessor.getDestination();
		String receiver = destination.substring(destination.lastIndexOf("/") + 1);
		
		byte[] payload = (byte[])message.getPayload();
		
		ReceiveMessage receiveMessage = deserializeReceiveMessage(payload);
		receiveMessage.setSender(authentication.getName());
		receiveMessage.setType(MessageType.CHAT);

        // Create a new message with the modified content
        Message<byte[]> modifiedMsg = MessageBuilder.withPayload(serializeReceiveMessage(receiveMessage))
                .copyHeaders(message.getHeaders())
                .setHeaders(accessor)
                .build();
        
        ChatTextMessage textMessage = new ChatTextMessage(receiveMessage.getSender(), receiver, receiveMessage.getMessage());
        chatTextMessagesService.saveMessage(textMessage);
        
        return modifiedMsg;
	}
	
	

	// Helper method to deserialize the payload (assuming it's a JSON representation
	// of ChatMessage)
	private ReceiveMessage deserializeReceiveMessage(byte[] payload) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(payload, ReceiveMessage.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private byte[] serializeReceiveMessage(ReceiveMessage receiveMessage) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		try {
			return objectMapper.writeValueAsBytes(receiveMessage);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
