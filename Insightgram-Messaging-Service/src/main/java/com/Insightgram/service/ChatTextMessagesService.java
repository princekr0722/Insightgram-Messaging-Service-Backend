package com.Insightgram.service;

import java.util.List;

import com.Insightgram.dto.LastConversationWith;
import com.Insightgram.dto.PageOf;
import com.Insightgram.entity.ChatTextMessage;

public interface ChatTextMessagesService {

	ChatTextMessage saveMessage(ChatTextMessage textMessage);
	ChatTextMessage getMessage(String receiver, Long messageId);
	ChatTextMessage deleteTextMessage(String receiver, Long messageId);
	Boolean editTextMessage(String receiver, Long messageId, String newMessage);
	PageOf<ChatTextMessage> getChatMessagesWith(String username, Integer pageSize, Integer pageNumber);
	public List<LastConversationWith> getAllConversations();
	
}
