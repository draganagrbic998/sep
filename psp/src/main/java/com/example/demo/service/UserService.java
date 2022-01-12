package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.Auth;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.TokenUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class UserService implements UserDetailsService {

	private final UserRepository repo;
	private final AuthenticationManager authManager;
	private final TokenUtils tokenUtils;
	private final PasswordEncoder passwordEncoder;
	private final RestTemplate restTemplate;

	@Override
	public User loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("UserService - loadUserByUsername: email=" + email);
		return repo.findByEmail(email);
	}

	public User findByApiKey(String apiKey) {
		log.info("UserService - findByApiKey: apiKey=" + apiKey);
		return repo.findByApiKey(apiKey);
	}

	public User getLoggedInUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public Auth login(Auth auth) {
		log.info("UserService - login: email=" + auth.getEmail());
		return new Auth((User) authManager
				.authenticate(new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()))
				.getPrincipal(), tokenUtils.generateToken(auth.getEmail()));
	}

	public List<User> read() {
		log.info("UserService - read");
		return repo.findAll();
	}

	public User readOne(Long id) {
		log.info("UserService - readOne");
		return repo.findById(id).get();
	}

	public User save(User user) {
		log.info("UserService - save: id=" + user.getId());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setApiKey(UUID.randomUUID().toString());

		if (!user.getRole().equals("psp-admin")) {
			user.setWebshopId(restTemplate
					.exchange(user.getWebshop() + "/users", HttpMethod.POST, new HttpEntity<>(user), User.class)
					.getBody().getId());
		}

		return repo.save(user);
	}

	public User save(Long id, User user) {
		log.info("UserService - save: id=" + user.getId());
		User old = repo.findById(id).get();
		old.setMethods(user.getMethods());
		return repo.save(old);
	}

	public void delete(Long id) {
		log.info("UserService - delete: id=" + id);
		User user = repo.findById(id).get();

		if (user.getWebshopId() != null) {
			restTemplate.exchange(user.getWebshop() + "/users/" + user.getWebshopId(), HttpMethod.DELETE, null,
					Void.class);
		}
		repo.deleteById(id);
	}

}
