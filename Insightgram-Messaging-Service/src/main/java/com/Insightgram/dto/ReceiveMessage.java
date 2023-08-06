package com.Insightgram.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveMessage implements Serializable{
	@JsonProperty(access = Access.READ_ONLY)
	private String sender;
	@NotNull
	private String message;
	
	@JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
	private LocalDateTime time = LocalDateTime.now();
	
	@JsonProperty(access = Access.READ_ONLY)
	private MessageType type;
	
	public enum MessageType {
		CHAT, JOIN, LEAVE
	}

	@Override
	public String toString() {
		return "ChatMessage [sender=" + sender + ", message=" + message + ", type=" + type + "]";
	}
}
