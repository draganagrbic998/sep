package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Auth;
import com.example.demo.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserService service;

	@PostMapping
	public ResponseEntity<Auth> login(@Valid @RequestBody Auth auth) {
		log.info("AuthController - login");
		return ResponseEntity.ok(service.login(auth));
	}

}