package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderStatusUpdate;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
@PreAuthorize("hasAuthority('merchant')")
@Log4j2
public class OrderController {

	private final OrderService service;

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusUpdate dto) {
		log.info("OrderController - updateStatus");
		if (id == null || dto.getId() == null || id != dto.getId()) {
			log.error("updateStatus - id is invalid");
			return ResponseEntity.badRequest().build();
		}
		service.updateStatus(id, dto.getStatus());
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<Order>> read() {
		log.info("OrderController - read");
		return ResponseEntity.ok(service.read());
	}

}
