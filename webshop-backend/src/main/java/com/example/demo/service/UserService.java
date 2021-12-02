package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Auth;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.TokenUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

	private final UserRepository repo;
	private final AuthenticationManager authManager;
	private final TokenUtils tokenUtils;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("UserService - loadUserByUsername: email=" + email);
		return repo.findByEmail(email);
	}

	public Auth login(Auth auth) {
		log.info("UserService - login: auth_email=" + auth.getEmail());
		return new Auth((User) authManager
				.authenticate(new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()))
				.getPrincipal(), tokenUtils.generateToken(auth.getEmail()));
	}

	public User getLoggedInUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
