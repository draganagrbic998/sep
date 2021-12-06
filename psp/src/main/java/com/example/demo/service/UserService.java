package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.Auth;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import com.example.demo.security.TokenUtils;
import com.example.demo.utils.DatabaseCipher;

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
	private final DatabaseCipher cipher;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("UserService - loadUserByUsername: email=" + email);
		return repo.findByEmail(email);
	}

	public User findByApiKey(String apiKey) {
		log.info("UserService - findByApiKey: apiKey=" + apiKey);
		return repo.findByApiKey(apiKey);
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

	@Transactional(readOnly = true)
	public List<User> read() {
		log.info("UserService - read");
		return repo.findAll();
	}

	@Transactional(readOnly = true)
	public User readOne(Long id) {
		log.info("UserService - readOne: id=" + id);
		return repo.findById(id).get();
	}

	@Transactional
	public User save(User user) {
		user.setApiKey(cipher.encrypt(UUID.randomUUID().toString()));
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		if (!user.getRole().equals("psp-admin")) {
			Long webshopId = restTemplate
					.exchange(user.getWebshop() + "/users", HttpMethod.POST, new HttpEntity<User>(user), User.class)
					.getBody().getId();
			user.setWebshopId(webshopId);
		}

		user = repo.save(user);
		log.info("UserService - save: id=" + user.getId());
		return user;
	}

	@Transactional
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
