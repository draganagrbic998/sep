package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.PaymentMethod;
import com.example.demo.service.PaymentMethodService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/payment-methods")
@AllArgsConstructor
public class PaymentMethodController {

	private final PaymentMethodService service;

	@GetMapping
	public ResponseEntity<List<PaymentMethod>> read() {
		log.info("PaymentMethodController - read");
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<PaymentMethod> readOne(@PathVariable Long id) throws NotFoundException {
		log.info("PaymentMethodController - readOne: id=" + id);
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	private ResponseEntity<PaymentMethod> create(@RequestBody PaymentMethod dto) {
		log.info("PaymentMethodController - create");
		return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PaymentMethod> update(@PathVariable Long id, @Valid @RequestBody PaymentMethod dto) {
		log.info("PaymentMethodController - update: id=" + id);

		if (id == null || dto.getId() == null || id != dto.getId()) {
			log.error("delete - id is invalid");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.save(dto));
	}

	@DeleteMapping("/{paymentMethodId}")
	public ResponseEntity<Void> remove(@PathVariable Long paymentMethodId) throws NotFoundException {
		log.info("PaymentMethodController - remove: id=" + paymentMethodId);
		service.remove(paymentMethodId);
		return ResponseEntity.noContent().build();
	}

}
