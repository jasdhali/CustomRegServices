package com.example.bootsecurity.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
	boolean isTokenValid(String token, UserDetails userDetails);
	String  getUsername(String token);
	String generateToken(UserDetails userDetails);
}	
