package com.example.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

	private final UserDetailsService userService;
	private final TokenUtils tokenUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = request.getHeader("Authorization");
		if (token != null && !token.isBlank()) {
			UserDetails user = userService.loadUserByUsername(tokenUtils.getEmail(token));
			if (user != null && tokenUtils.validateToken(user, token)) {
				SecurityContextHolder.getContext().setAuthentication(new AuthToken(user, token));
			}
		}
		filterChain.doFilter(request, response);
	}

}
