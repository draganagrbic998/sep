package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/payment-methods")
@Log4j2
public class PaymentMethodController {

	private final PaymentMethodService service;

	@GetMapping
	public ResponseEntity<List<PaymentMethod>> read() {
		log.info("PaymentMethodController - read");
		return ResponseEntity.ok(service.read());
	}

	@PostMapping
	private ResponseEntity<PaymentMethod> create(@RequestBody PaymentMethod dto) {
		log.info("PaymentMethodController - create");

		if (dto.getId() != null) {
			log.error("delete - dto id not null");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.save(dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.info("PaymentMethodController - delete: id=" + id);

		if (id == null) {
			log.error("delete - id is null");
			return ResponseEntity.badRequest().build();
		}
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/to_add")
	public ResponseEntity<List<String>> toAdd() {
		log.info("PaymentMethodController - toAdd");
		return ResponseEntity.ok(service.toAdd());
	}

}
