package com.Insightgram.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LastConversationWith {
	private String conversationWith;
	@JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
	private LocalDateTime time;

	private String sender;
	private String message;
	public LastConversationWith(LocalDateTime time, String conversationWith) {
		this.conversationWith = conversationWith;
		this.time = time;
	}
}
