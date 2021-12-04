package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

	private final DatabaseCipher cipher;

	private final UserService service;

	@GetMapping("/{id}")
	public ResponseEntity<User> readOne(@PathVariable Long id) {
		log.info("UserController - readOne: id=" + id);
		return ResponseEntity.ok(cipher.decrypt(service.readOne(id)));
	}

	@PostMapping
	public ResponseEntity<User> create(@Valid @ModelAttribute User dto) {
		log.info("UserController - create");

		if (dto.getId() != null) {
			log.error("create - dto id not null");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.save(dto));
	}

}
