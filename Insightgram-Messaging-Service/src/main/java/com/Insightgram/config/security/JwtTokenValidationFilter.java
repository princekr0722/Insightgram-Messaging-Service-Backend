package com.Insightgram.config.security;

import java.io.IOException;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Insightgram.respository.AccessTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenValidationFilter extends OncePerRequestFilter {

	@Autowired
	private AccessTokenRepository accessTokenRepository;

	private String invalidTokenMessage = "Invalid token received!";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String jwt = request.getHeader(SpringSecurityConstants.JWT_HEADER);
		if (jwt != null) {

			Authentication authentication = getUsernamePasswordAuthenticationToken(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);

		}

		filterChain.doFilter(request, response);
	}

	public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String jwt) {
		try {
			jwt = jwt.substring(7);

			isTokenValid(jwt);

			SecretKey key = Keys.hmacShaKeyFor(SpringSecurityConstants.JWT_KEY.getBytes());

			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

			String username = (String) claims.get("username");
//				String authoritiesString = (String) claims.get("authorities");
			//
//				List<GrantedAuthority> authorities = AuthorityUtils
//						.commaSeparatedStringToAuthorityList(authoritiesString);

			return new UsernamePasswordAuthenticationToken(username, null, null);
		} catch (Exception e) {
			throw new BadCredentialsException(invalidTokenMessage);
		}
	}

	private void isTokenValid(String accessToken) {
		boolean tokenExists = accessTokenRepository.existsByAccessToken(accessToken);
		if (!tokenExists) {
			throw new BadCredentialsException(invalidTokenMessage);
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals("/user/chat/accessToken")
				|| request.getServletPath().equals("/user/chat/register");
	}
}
