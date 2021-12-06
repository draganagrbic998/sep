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

import com.example.demo.model.PaymentMethod;
import com.example.demo.service.PaymentMethodService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/payment-methods")
@PreAuthorize("hasAuthority('psp-admin')")
public class PaymentMethodController {

	private final PaymentMethodService service;

	@GetMapping
	public ResponseEntity<List<PaymentMethod>> read() {
		return ResponseEntity.ok(service.read());
	}

	@PostMapping
	public ResponseEntity<PaymentMethod> create(@RequestBody PaymentMethod dto) {
		return ResponseEntity.ok(service.save(dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/to_add")
	public ResponseEntity<List<String>> toAdd() {
		return ResponseEntity.ok(service.toAdd());
	}

}
