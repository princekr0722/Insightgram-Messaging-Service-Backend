package com.Insightgram.config.security;

import java.io.IOException;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {

			SecretKey key = Keys.hmacShaKeyFor(SpringSecurityConstants.JWT_KEY.getBytes());

			String jwt = Jwts.builder()
					.setIssuer("Prince Kumar")
					.setSubject("Insightgram Chat application JWT")
					.claim("username", authentication.getName())
					.setIssuedAt(new Date())
//					.setExpiration(new Date(new Date().getTime() + 300000000))
					.signWith(key).compact();

			response.setHeader(SpringSecurityConstants.JWT_HEADER, jwt);
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !request.getServletPath().equals("/user/chat/accessToken");
	}

}
