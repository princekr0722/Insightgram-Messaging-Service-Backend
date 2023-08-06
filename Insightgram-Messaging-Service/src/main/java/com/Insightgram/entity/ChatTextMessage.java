package com.Insightgram.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatTextMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	
	@Column(nullable = false)
	private String sender;
	
	@Column(nullable = false)
	private String receiver;
	
	@Column(nullable = false, length = 4000)
	private String message;
	
	@Column(nullable = false)
	private boolean isDeleted;
	
	@Column(nullable = false)
	@JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
	private LocalDateTime time = LocalDateTime.now();

	public ChatTextMessage(String sender, String receiver, String message) {
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
	}

	public ChatTextMessage(Object id, Object sender, Object receiver, Object message, Object isDeleted,
			Object time) {
		super();
		this.id = (Long)id;
		this.sender = (String)sender;
		this.receiver = (String)receiver;
		this.message = (String)message;
		this.isDeleted = (Integer)isDeleted == 1?true:false;
		this.time = (LocalDateTime)time;
	}
	
	
	
}
