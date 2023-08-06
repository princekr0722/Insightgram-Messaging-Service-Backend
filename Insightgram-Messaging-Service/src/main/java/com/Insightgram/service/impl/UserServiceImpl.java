package com.Insightgram.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Insightgram.dto.UserInfoDto;
import com.Insightgram.entity.UserInfo;
import com.Insightgram.exception.AccessTokenException;
import com.Insightgram.respository.AccessTokenRepository;
import com.Insightgram.respository.UserInfoRepository;
import com.Insightgram.service.UserService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private AccessTokenRepository accessTokenRepository;
	
	
	@Override
	public UserInfoDto registerUser(UserInfo userInfo) {
		return new UserInfoDto(userInfoRepository.save(userInfo));
	}
	
	@Override
	public void destroyAccessToken(String accessToken, String sessionId) {
		accessToken = accessToken.substring(7);
		int rowEdited = accessTokenRepository.deleteByAccessToken(accessToken, sessionId);
		if(rowEdited==0) {
			throw new AccessTokenException("No access token found.");
		}
	}

}
