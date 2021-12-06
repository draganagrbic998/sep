package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
@PreAuthorize("hasAuthority('psp-admin')")
public class UserController {

	private final UserService service;

	@GetMapping
	public ResponseEntity<List<User>> read() {
		return ResponseEntity.ok(service.read());
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> readOne(@PathVariable Long id) {
		return ResponseEntity.ok(service.readOne(id));
	}

	@PostMapping
	public ResponseEntity<User> create(@RequestBody User dto) {
		return ResponseEntity.ok(service.save(dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
