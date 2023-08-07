package com.Insightgram.config.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class SecurityConfiguration {
	
	@Value("${allowed.frontend.origin}")
	private String allowedFrontendOrigin;
	
	@Value("${allowed.backend.origin}")
	private String allowedBackendOrigin;
	
	@Value("${allowed.api.gateway}")
	private String allowedApiGateway;
	
	@Autowired
	private JwtTokenGeneratorFilter jwtTokenGeneratorFilter;
	
	@Autowired
	private JwtTokenValidationFilter jwtTokenValidationFilter;
	
	private final String[] WEBSOCKET_ENDPOINTS = {"/ws/**", "/chat-app/**", "/queue/**", "topic/**"}; 
	
	private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
    };
	
	@Bean
	SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity
			.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(httpRequest->{ httpRequest
				.requestMatchers(WEBSOCKET_ENDPOINTS).permitAll() 
				.requestMatchers(AUTH_WHITE_LIST).permitAll().
				anyRequest().authenticated();
			})
			.addFilterAfter(jwtTokenGeneratorFilter, BasicAuthenticationFilter.class)
			.addFilterBefore(jwtTokenValidationFilter, BasicAuthenticationFilter.class)
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(getConfigurationSource()))
			.formLogin(Customizer.withDefaults())
			.httpBasic(Customizer.withDefaults());
		
		return httpSecurity.build();
	}
	
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CorsConfigurationSource getConfigurationSource() {
		CorsConfiguration configurationChatSocketFrontend = new CorsConfiguration();
		configurationChatSocketFrontend.setAllowedOrigins(Arrays.asList(allowedFrontendOrigin));
		configurationChatSocketFrontend.setAllowedHeaders(Arrays.asList("*"));
		configurationChatSocketFrontend.setExposedHeaders(Arrays.asList("*"));
		configurationChatSocketFrontend.setAllowedMethods(Arrays.asList("*"));
		configurationChatSocketFrontend.setAllowCredentials(true);
		
		CorsConfiguration corsConfigurationBackend = new CorsConfiguration();
		corsConfigurationBackend.setAllowedOrigins(Arrays.asList(allowedBackendOrigin));
		corsConfigurationBackend.setAllowedHeaders(Arrays.asList("*"));
		corsConfigurationBackend.setExposedHeaders(Arrays.asList("*"));
		corsConfigurationBackend.setAllowedMethods(Arrays.asList("*"));
		corsConfigurationBackend.setAllowCredentials(false);
		corsConfigurationBackend.setMaxAge((long) 0);
		
		CorsConfiguration corsConfigurationAll = new CorsConfiguration();
		corsConfigurationAll.setAllowedOrigins(Arrays.asList(allowedBackendOrigin, allowedFrontendOrigin, allowedApiGateway));
		corsConfigurationAll.setAllowedHeaders(Arrays.asList("*"));
		corsConfigurationAll.setAllowedMethods(Arrays.asList("*"));
		corsConfigurationAll.setExposedHeaders(Arrays.asList("*"));
		corsConfigurationAll.setAllowCredentials(true);
		
		CorsConfiguration corsConfigurationAllForTest = new CorsConfiguration();
		corsConfigurationAllForTest.setAllowedOrigins(Arrays.asList("*"));
		corsConfigurationAllForTest.setAllowedHeaders(Arrays.asList("*"));
		corsConfigurationAllForTest.setAllowedMethods(Arrays.asList("*"));
		corsConfigurationAllForTest.setExposedHeaders(Arrays.asList("*"));
		corsConfigurationAllForTest.setAllowCredentials(false);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/chat-app/**", corsConfigurationAll);
		source.registerCorsConfiguration("/queue/**", corsConfigurationAll);
		source.registerCorsConfiguration("/ws/**", corsConfigurationAll);
		
		source.registerCorsConfiguration("/user/chat/**", corsConfigurationBackend);
		
		return source;
	}
}
