package com.Insightgram.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Insightgram.config.security.SpringSecurityConstants;
import com.Insightgram.dto.UserInfoDto;
import com.Insightgram.entity.AccessToken;
import com.Insightgram.entity.UserInfo;
import com.Insightgram.exception.UserException;
import com.Insightgram.respository.AccessTokenRepository;
import com.Insightgram.respository.UserInfoRepository;
import com.Insightgram.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;

@RestController
@RequestMapping("/user/chat")
public class FrontController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private AccessTokenRepository accessTokenRepository;
	
	@PostMapping("/register")
	public ResponseEntity<UserInfoDto> registerUser(@Valid @RequestBody UserInfo userInfo) {
		return new ResponseEntity<UserInfoDto>(userService.registerUser(userInfo), HttpStatus.OK);
	}
	
	@GetMapping("/accessToken")
	public ResponseEntity<String> getAccessToken(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
		String jwt = httpServletResponse.getHeader(SpringSecurityConstants.JWT_HEADER);
		String sessionJwt = httpServletRequest.getHeader(SpringSecurityConstants.SESSION_JWT_HEADER);
		
		if(sessionJwt == null) throw new BadRequestException("Session JWT is missing in the header");
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Optional<UserInfo> opt = userInfoRepository.findByUsername(username);
		if(opt.isEmpty()) throw new UserException("No user found.");
		
		UserInfo userInfo = opt.get(); 
		AccessToken accessToken = new AccessToken(jwt, sessionJwt, userInfo);
		
		accessTokenRepository.save(accessToken);
		return new ResponseEntity<>(jwt, HttpStatus.OK);
	}
	
	@DeleteMapping("/remove/accessToken")
	public void destroyAccessToken(HttpServletRequest request) {
		String jwt = request.getHeader(SpringSecurityConstants.JWT_HEADER);
		String sessionJwt = request.getHeader(SpringSecurityConstants.SESSION_JWT_HEADER);
		if(sessionJwt == null) throw new BadRequestException("Session ID is missing in the header");
		
		userService.destroyAccessToken(jwt, sessionJwt);
	}
}
