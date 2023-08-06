package com.Insightgram.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.Insightgram.dto.LastConversationWith;
import com.Insightgram.entity.ChatTextMessage;

public interface ChatTextMessageRepository extends JpaRepository<ChatTextMessage, Long>, PagingAndSortingRepository<ChatTextMessage, Long>{
	
	@Query("SELECT m FROM ChatTextMessage m WHERE m.id=:messageId AND m.sender = :sender AND m.receiver=:receiver")
	Optional<ChatTextMessage> getChatMessageBy(String sender, String receiver, Long messageId);

	@Modifying
	@Query("UPDATE ChatTextMessage SET message = :newMessage WHERE id=:messageId AND sender=:sender AND receiver=:receiver")
	int editMessageBy(String sender, String receiver, Long messageId, String newMessage);
	
	@Modifying
	@Query("UPDATE ChatTextMessage SET message = 'This message is deleted' WHERE id=:messageId AND sender=:sender AND receiver=:receiver")
	Optional<ChatTextMessage> markMessageAsDeleted(Long messageId, String sender, String receiver);
	
	@Query("SELECT m FROM ChatTextMessage m WHERE (sender=:person1 AND receiver=:person2) OR (sender=:person2 AND receiver=:person1) ORDER BY time DESC")
	Page<ChatTextMessage> getMessagesBetween(String person1, String person2, Pageable pageable);
	
//	@Query("SELECT DISTINCT new com.Insightgram.entity.ChatTextMessage(id, sender, receiver, message, isDeleted, MAX(time)) FROM ChatTextMessage WHERE sender = :username OR receiver = :username Group By CASE  WHEN sender = :username THEN receiver WHEN receiver = :username THEN sender END")
//	@Query("SELECT MAX(time), "
//			+ "CASE "
//			+ "WHEN sender = :username THEN receiver "
//			+ "WHEN receiver = :username THEN sender "
//			+ "END, "
//			+ "message "
//			+ "FROM ChatTextMessage WHERE sender = :username OR receiver = :username ")
	@Query("SELECT NEW com.Insightgram.dto.LastConversationWith(MAX(time), "
			+ "CASE "
			+ "WHEN sender = :username THEN receiver "
			+ "WHEN receiver = :username THEN sender "
			+ "END as conversationWith) "
			+ "FROM ChatTextMessage "
			+ "WHERE sender = :username OR receiver = :username "
			+ "Group By conversationWith")
	List<LastConversationWith> getAllConversationsOf(String username);
	
	@Query("Select time, sender, message "
			+ "From ChatTextMessage "
			+ "WHERE (sender = :person1 AND receiver = :person2) "
			+ "OR (sender = :person2 AND receiver = :person1) "
			+ "ORDER BY time DESC")
	Page<Object[]> getLastMessages(String person1, String person2, Pageable pageable);
	
}
