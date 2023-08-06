package com.Insightgram.dto;

import com.Insightgram.entity.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoDto {
	private Integer id;
	private String username;
	
	public UserInfoDto(UserInfo userInfo) {
		this.id = userInfo.getId();
		this.username = userInfo.getUsername();
	}
}
