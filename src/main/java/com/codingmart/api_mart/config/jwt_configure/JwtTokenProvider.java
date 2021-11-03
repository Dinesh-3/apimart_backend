package com.codingmart.api_mart.config.jwt_configure;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;

import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Configuration
public class JwtTokenProvider implements Serializable {

	private static final long serialVersionUID = 2569800841756370596L;
	private static final long validityInMilliseconds = 1000 * 60 * 60 * 60;
	@Value("${jwt.secret-key}")
	private String secretKey;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(String username, String email, String id) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("email", email);
		claims.put("id", id);
		Date now = new Date();
		return Jwts.builder().setClaims(claims).setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + validityInMilliseconds))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public Authentication getAuthentication(String token) {
		String username = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public User getUser(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		return userRepository.findById((String) claims.get("id"));
	}

}
