package com.Insightgram.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-app")
public class TestController {
	
	@GetMapping("/hello")
	public String sayHello() {
		return "Hello "+SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
