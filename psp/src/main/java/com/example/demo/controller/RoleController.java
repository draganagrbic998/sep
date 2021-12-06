package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Role;
import com.example.demo.service.RoleService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('psp-admin')")
public class RoleController {

	private final RoleService service;

	@GetMapping
	public ResponseEntity<List<Role>> read() {
		return ResponseEntity.ok(service.read());
	}

}
