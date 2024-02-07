package com.example.bootsecurity.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bootsecurity.reposiroty.AppuserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final AppuserRepository appuserRepository;
	
	@Override
	public UserDetailsService getUserDetailsService() {
		// TODO Auto-generated method stub
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {				
				return appuserRepository.findByEmail(username).orElseThrow( ()-> new UsernameNotFoundException("user not found"));
			}
		};
	}
	
}
