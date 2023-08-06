package com.Insightgram.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Insightgram.dto.LastConversationWith;
import com.Insightgram.dto.PageOf;
import com.Insightgram.entity.ChatTextMessage;
import com.Insightgram.service.ChatTextMessagesService;

@RestController
@RequestMapping("/chat-app")
public class ChatMessageController {

	@Autowired
	private ChatTextMessagesService textMessagesService;
	
	@GetMapping("/{username}/message/{messageId}")
	public ResponseEntity<ChatTextMessage> getMessage(@PathVariable String username,@PathVariable Long messageId) {
		ChatTextMessage message = textMessagesService.getMessage(username, messageId);
		return new ResponseEntity<ChatTextMessage>(message, HttpStatus.OK);
	}

	@DeleteMapping("/{receiver}/message/{messageId}")
	public ResponseEntity<ChatTextMessage> deleteTextMessage(@PathVariable String receiver,@PathVariable Long messageId) {
		ChatTextMessage message = textMessagesService.deleteTextMessage(receiver, messageId);
		return new ResponseEntity<ChatTextMessage>(message, HttpStatus.OK);
	}

	@PutMapping("/{receiver}/message/{messageId}")
	public ResponseEntity<Boolean> editTextMessage(@PathVariable String receiver, @PathVariable Long messageId, @RequestParam String newMessage) {
		Boolean edited = textMessagesService.editTextMessage(receiver, messageId, newMessage);
		return new ResponseEntity<Boolean>(edited, HttpStatus.OK);
	}

	@GetMapping("/{username}/messages")
	public ResponseEntity<PageOf<ChatTextMessage>> getChatMessagesWith(@PathVariable String username, @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
		PageOf<ChatTextMessage> pageOfChatTextMessages = textMessagesService.getChatMessagesWith(username, pageSize, pageNumber);
		return new ResponseEntity<PageOf<ChatTextMessage>>(pageOfChatTextMessages, HttpStatus.OK);
	}

	@GetMapping("/message/conversations")
	public ResponseEntity<List<LastConversationWith>> getAllConversations() {
		List<LastConversationWith> conversations = textMessagesService.getAllConversations();
		return new ResponseEntity<>(conversations, HttpStatus.OK);
	}
	
}
