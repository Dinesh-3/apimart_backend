package com.codingmart.api_mart.config.jwt_configure;


import com.codingmart.api_mart.ExceptionHandler.ClientErrorException;
import com.codingmart.api_mart.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class JwtTokenFilter extends OncePerRequestFilter {

	private final JwtTokenProvider tokenProvider;

	public JwtTokenFilter(JwtTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader("Authorization");
		try {
			if (token != null) {
				User user = tokenProvider.getUser(token);
				request.setAttribute("user", user);
				SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(token));
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new ClientErrorException(HttpStatus.FORBIDDEN, String.format("Expired or invalid token %s", e.getMessage()));
		}finally {
//			SecurityContextHolder.clearContext(); // Error Line
		}
		filterChain.doFilter(request, response);
	}

}
