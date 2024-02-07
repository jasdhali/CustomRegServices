package com.example.bootsecurity.config;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.bootsecurity.model.ApplicationUser;
import com.example.bootsecurity.model.UsernamePasswordAuthenticationToken;
import com.example.bootsecurity.reposiroty.AppuserRepository;
import com.example.bootsecurity.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
	
	private final JwtService jwtTokenUtil;
	private final AppuserRepository userRepo;	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//Get Auth header and validate
		final String header = request.getHeader("Authorization");
		if( header==null || header.length() == 0 || !header.startsWith("Bearer ") ) {
			filterChain.doFilter(request, response);
			return;
		}
		//Get jwt token and validate
		final String token =  header.split(" ")[1].trim();
		ApplicationUser applicationUser = 
				userRepo.findByEmail( jwtTokenUtil.getUsername(token) ).orElse(null);
		if(!jwtTokenUtil.isTokenValid(token,applicationUser)) {
			filterChain.doFilter(request, response);
			return;			
		}		
		
		
		UsernamePasswordAuthenticationToken
			authentication = new UsernamePasswordAuthenticationToken(applicationUser.getAuthorities(),
					applicationUser.getId(),applicationUser.getEmail(), applicationUser.getFirstName(),
					applicationUser.getSecondName(),applicationUser.getRole().ordinal());
		authentication.setAuthenticated(true);
		
		authentication.setDetails(
	            new WebAuthenticationDetailsSource().buildDetails(request)
	        );
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}
}
