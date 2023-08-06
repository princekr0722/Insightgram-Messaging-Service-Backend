package com.Insightgram.config.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.Insightgram.respository.UserInfoRepository;

@Component
public class ApplicationAuthenticationProvider implements AuthenticationProvider{

	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		if(!SpringSecurityConstants.BACKEND_CHAT_ACCESS_PASSWORD.equals(password)) {
			throw new BadCredentialsException("Access Denied!");
		}
		
//		boolean userExists = userInfoRepository.existsByUsername(username);
//		if(userExists) {
			Set<GrantedAuthority> authorities = new HashSet<>();
			return new UsernamePasswordAuthenticationToken(username, null, authorities);
		
//		} else {
//			throw new BadCredentialsException("No user found with username: "+username);
//		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
