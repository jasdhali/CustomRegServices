package com.example.bootsecurity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
//import com.example.bootsecurity.model.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bootsecurity.AuditActions;
import com.example.bootsecurity.dto.AppUserDTO;
import com.example.bootsecurity.dto.AuditLogRequestDTO;
import com.example.bootsecurity.dto.JwtAuthenticationResponse;
import com.example.bootsecurity.dto.SigninRequest;
import com.example.bootsecurity.model.ApplicationUser;
import com.example.bootsecurity.reposiroty.AppuserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private final AppuserRepository appuserRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager; 
	private final JwtService jwtService;
	private final MessageBrokerDelegate messagesService;
	
	@Override
	public String signUpAppUser(AppUserDTO appUserDTO) {
		ApplicationUser user =  AppUserDTO.to(appUserDTO,passwordEncoder);
		appuserRepository.save(user);
		messagesService.userSignedUp(user);
		AuditLogRequestDTO auditLogRequest = AuditLogRequestDTO.builder()				
				.userId( user.getId() )
				.accountId( null )
				.artifactId( user.getId() )
				.artifactName( "USER" )
				.actionPerformed( AuditActions.INSERT )
				.build();
		messagesService.createAuditMessage(auditLogRequest);
		return "User successfully registered";
	}
	
	@Override
	public JwtAuthenticationResponse signIn(SigninRequest signinRequest) {
		
		try {
			authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),
					signinRequest.getPassword() ));
			
		} catch (AuthenticationException e) {			
			System.out.println( e.getMessage() );
		}
		
		Optional<ApplicationUser> applicationUser = appuserRepository.findByEmail(signinRequest.getEmail());		
		return JwtAuthenticationResponse.builder()
				.refreshToken("refreshToken")
				.token(jwtService.generateToken( (ApplicationUser)applicationUser.get() ))
				.build();
	}
	@Override
	public List<AppUserDTO> getUsers(){
		List<ApplicationUser> applicationUsers = appuserRepository.findAll();
		return applicationUsers.stream().map(AppUserDTO::from).toList();
	}

}
