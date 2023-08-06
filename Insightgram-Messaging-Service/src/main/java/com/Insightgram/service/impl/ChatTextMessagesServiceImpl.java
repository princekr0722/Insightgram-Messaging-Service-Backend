package com.Insightgram.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.MessagingException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Insightgram.dto.LastConversationWith;
import com.Insightgram.dto.PageOf;
import com.Insightgram.entity.ChatTextMessage;
import com.Insightgram.respository.ChatTextMessageRepository;
import com.Insightgram.service.ChatTextMessagesService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ChatTextMessagesServiceImpl implements ChatTextMessagesService{

	@Autowired
	private ChatTextMessageRepository chatTextMessageRepository;
	
	@Override
	public ChatTextMessage saveMessage(ChatTextMessage textMessage) {
		ChatTextMessage savedMessage = chatTextMessageRepository.save(textMessage);
		return savedMessage;
	}
	
	@Override
	public ChatTextMessage getMessage(String receiver, Long messageId) {
		String sender = SecurityContextHolder.getContext().getAuthentication().getName();
		ChatTextMessage message = chatTextMessageRepository.getChatMessageBy(sender, receiver, messageId)
				.orElseThrow(()-> new MessagingException("No such message is avaialbe between "+sender+""));
		return message;
	}

	@Override
	public ChatTextMessage deleteTextMessage(String receiver, Long messageId) {
		String sender = SecurityContextHolder.getContext().getAuthentication().getName();
		ChatTextMessage message = chatTextMessageRepository.markMessageAsDeleted(messageId, sender, receiver)
				.orElseThrow(()-> new MessagingException("No such message is avaialbe between "+sender+""));
		return message;
	}

	@Override
	public Boolean editTextMessage(String receiver, Long messageId, String newMessage) {
		String sender = SecurityContextHolder.getContext().getAuthentication().getName();
		int rowEdited = chatTextMessageRepository.editMessageBy(sender, receiver, messageId, newMessage);
		
		if(rowEdited == 0) throw new MessagingException("No such message is avaialbe between "+sender+"");
		return true;
	}

	@Override
	public PageOf<ChatTextMessage> getChatMessagesWith(String username, Integer pageSize,
			Integer pageNumber) {
		if(pageNumber <= 0) throw new IllegalArgumentException("Page number cannot be less than equals to 0");
		pageNumber--;
		String person1 = SecurityContextHolder.getContext().getAuthentication().getName();
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<ChatTextMessage> pageOfMessages = chatTextMessageRepository.getMessagesBetween(person1, username, pageable);
		return new PageOf<>(pageOfMessages);
	}

	@Override
	public List<LastConversationWith> getAllConversations() {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		List<LastConversationWith> conversations = chatTextMessageRepository.getAllConversationsOf(user);
		
		//add last messages and senders
		conversations.forEach(convo -> {
			Pageable pageable = PageRequest.of(0, 1);
			
			Page<Object[]> page = chatTextMessageRepository.getLastMessages(user, convo.getConversationWith(), pageable); 
			convo.setSender((String)page.getContent().get(0)[1]);
			convo.setMessage((String)page.getContent().get(0)[2]);
		});
		return conversations;
	}

}
