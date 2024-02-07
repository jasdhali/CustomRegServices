package com.example.bootsecurity.service;

import java.util.List;

import com.example.bootsecurity.dto.AppUserDTO;
import com.example.bootsecurity.dto.JwtAuthenticationResponse;
import com.example.bootsecurity.dto.SigninRequest;

public interface AuthenticationService {
	String signUpAppUser(AppUserDTO appUserDTO);
	JwtAuthenticationResponse signIn(SigninRequest signinRequest);
	List<AppUserDTO> getUsers();
}
