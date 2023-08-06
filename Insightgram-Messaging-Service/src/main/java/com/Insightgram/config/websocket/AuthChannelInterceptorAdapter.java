package com.Insightgram.config.websocket;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.Insightgram.config.security.JwtTokenValidationFilter;
import com.Insightgram.config.security.SpringSecurityConstants;
import com.Insightgram.dto.ReceiveMessage;
import com.Insightgram.dto.ReceiveMessage.MessageType;
import com.Insightgram.exception.UserException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
	
	@Autowired
	private JwtTokenValidationFilter jwtTokenValidationFilter;
	
	@Autowired
	private SendMessageChannel sendMessageChannel;

	@Override
	public Message<?> preSend(final Message<?> message, final MessageChannel channel) throws AuthenticationException {
		final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		String jwt = accessor.getFirstNativeHeader(SpringSecurityConstants.JWT_HEADER);
		if (StompCommand.CONNECT == accessor.getCommand()) {
			final UsernamePasswordAuthenticationToken user = getUserPassAuth(jwt);
			
//			Here we can notify the other users that the connected user is online
			accessor.setUser(user);
		} else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
			String destination = accessor.getDestination();
			String username = destination.substring(destination.lastIndexOf("/") + 1);

			final UsernamePasswordAuthenticationToken user = getUserPassAuth(jwt);

			if (username.equals(user.getName())) {
				accessor.setUser(user);
			} else {
				throw new UserException("User is trying to subscribe another user's queue.");
			}
		} else if (StompCommand.SEND == accessor.getCommand()) {
//			String destination = accessor.getDestination();
//			String receiver = destination.substring(destination.lastIndexOf("/") + 1);

			final UsernamePasswordAuthenticationToken authentication = getUserPassAuth(jwt);
			accessor.removeNativeHeader(SpringSecurityConstants.JWT_HEADER);

	        // Create a new message with the modified content
	        Message<byte[]> modifiedMsg = sendMessageChannel.getSendableMessage(message, accessor, authentication);
			return modifiedMsg; 
		}
		return message;
	}

	private UsernamePasswordAuthenticationToken getUserPassAuth(String jwt) {
		final UsernamePasswordAuthenticationToken authentication = jwtTokenValidationFilter
				.getUsernamePasswordAuthenticationToken(jwt);
		return authentication;
	}
	
	
}