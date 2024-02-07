package com.example.bootsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SigninRequest {
	//@NotEmpty(message = "Email is mandatory")
	private String email;
	//@NotEmpty(message = "Pwd is mandatory")
	private String password;
	
}
