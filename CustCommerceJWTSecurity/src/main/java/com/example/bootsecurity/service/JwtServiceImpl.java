package com.example.bootsecurity.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.bootsecurity.model.ApplicationUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
	@Value("security.jwt.secret:JwtSecretKey")
	private String key;

	private Clock clock = DefaultClock.INSTANCE;
	
	public String generateToken(UserDetails userDetails) {
		ApplicationUser user = (ApplicationUser)userDetails;
		Map<String, Object> claims = new HashMap<>();
		//claims.put("user_id", token.getUserId());
		claims.put("email", user.getEmail());
		claims.put("name", user.getUsername());		
		//claims.put("account_id", user.getAccountId());
		//claims.put("company_name", token.getCompanyName());
		claims.put("role", user.getRole());
		//claims.put("roles", token.getRoles() != null ? String.join(",", token.getRoles()) : "");
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject( user.getFirstName() + "," + user.getSecondName())
				.setIssuedAt( clock.now() )
				.setExpiration( new Date(clock.now().getTime() + 30*60*1000 )) 
				.signWith( getSignInKey() ,SignatureAlgorithm.HS256 )
				.compact();
	}
	private Key getSignInKey() {
		//byte[] key = Decoders.BASE64.decode("RGV2Snd0U2VjcmV0");
		byte[] key = Decoders.BASE64.decode("saisaisaisaisaisaisaisaisaisaisasaisassss1234567891234569");		
		return Keys.hmacShaKeyFor(key);
	}
	public boolean isTokenValid(String token, UserDetails userDetails) {
		return !isTokenExpired(token);
	}

	public String getUsername(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return ""+claims.get("email");
		//return getClaimFromToken(token, Claims::getSubject);
	}

	private Boolean isTokenExpired(String token) {
		final Date expired = getExpirationDateFromToken(token);
		return expired.before(new Date());
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	/*public Claims getAllClaimsFromToken(String token) {
		return (Claims) Jwts.parserBuilder().requireAudience("string").build().parse(token).getBody();
		// return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	}*/
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}
	
}