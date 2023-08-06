package com.Insightgram.service;

import com.Insightgram.dto.UserInfoDto;
import com.Insightgram.entity.UserInfo;

public interface UserService {
	
	UserInfoDto registerUser(UserInfo userInfo);
	void destroyAccessToken(String accessToken, String sessionId);
	
}
