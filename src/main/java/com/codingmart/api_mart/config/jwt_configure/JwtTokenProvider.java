package com.codingmart.api_mart.config.jwt_configure;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider implements Serializable {

	private static final long serialVersionUID = 2569800841756370596L;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	private long validityInMilliseconds = 1000 * 60 * 60 * 60;

	public String createToken(String username, String email, String id) {
		System.out.println("username = " + username);
		System.out.println("email = " + email);
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("email", email);
		claims.put("id", id);
		Date now = new Date();
		return Jwts.builder().setClaims(claims).setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + validityInMilliseconds))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	@Autowired
	private UserDetailsService userDetailsService;

	public Authentication getAuthentication(String token) {
		String username = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public boolean validateToken(String token) {
		Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		return true;
	}

}
