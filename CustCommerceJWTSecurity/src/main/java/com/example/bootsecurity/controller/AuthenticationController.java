package com.example.bootsecurity.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootsecurity.dto.AppUserDTO;
import com.example.bootsecurity.dto.JwtAuthenticationResponse;
import com.example.bootsecurity.dto.SigninRequest;
import com.example.bootsecurity.response.ResponseHandler;
import com.example.bootsecurity.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody AppUserDTO appUserDTO) {
		
		return new ResponseEntity(authenticationService.signUpAppUser(appUserDTO), HttpStatus.OK);
	}

	@PostMapping("/signin")
	public ResponseEntity<Object> login(@RequestBody SigninRequest signinRequest) {
		
		JwtAuthenticationResponse response = authenticationService.signIn(signinRequest);
		ResponseEntity<Object> responseEntity =
				ResponseHandler.generateResponse("Successfully retrieved data!", 
						HttpStatus.OK, 
						authenticationService.signIn(signinRequest));
		return responseEntity;
		//return new ResponseEntity(response, HttpStatus.OK);
	}

	@GetMapping("/users")
	public ResponseEntity<List<AppUserDTO>> users() {
		return new ResponseEntity<>(authenticationService.getUsers(), HttpStatus.OK);
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello";
	}
	

}