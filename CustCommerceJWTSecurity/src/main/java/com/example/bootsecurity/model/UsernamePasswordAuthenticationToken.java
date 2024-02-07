package com.example.bootsecurity.model;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
	//private final transient Object principal;
	//private final transient Object credential;

	private int userId;
	private String email;
	private String firstName;
	private String secndName;	
	private int roleId;
	private String password;
	

	public UsernamePasswordAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
	}

	public UsernamePasswordAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String email) {
		super(authorities);
		this.email = email;
	}


	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	public UsernamePasswordAuthenticationToken(Collection<? extends GrantedAuthority> authorities, int userId,
			String email, String firstName, String secndName, int roleId) {
		super(authorities);
		this.userId = userId;
		this.email = email;
		this.firstName = firstName;
		this.secndName = secndName;
		this.roleId = roleId;
	}	
}