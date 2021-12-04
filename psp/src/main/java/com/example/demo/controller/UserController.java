package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService service;

	@GetMapping
	public ResponseEntity<List<User>> read() {
		log.info("UserController - read");
		return ResponseEntity.ok(service.read());
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> readOne(@PathVariable Long id) {
		log.info("UserController - readOne: id=" + id);
		return ResponseEntity.ok(service.readOne(id));
	}

	@PostMapping
	public ResponseEntity<User> create(@Valid @RequestBody User dto) {
		log.info("UserController - create");

		if (dto.getId() != null) {
			log.error("create - dto id not null");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.save(dto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User dto) {
		log.info("UserController - update: id=" + id);

		if (id == null || dto.getId() == null || id != dto.getId()) {
			log.error("update - id is invalid");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.save(dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.info("UserController - delete: id=" + id);

		if (id == null) {
			log.error("delete - id is null");
			return ResponseEntity.badRequest().build();
		}
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
