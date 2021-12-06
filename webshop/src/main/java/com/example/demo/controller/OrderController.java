package com.example.demo.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderStatusUpdate;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService service;

	@GetMapping
	public ResponseEntity<List<Order>> read() {
		return ResponseEntity.ok(service.read());
	}

	@PostMapping("/{productId}/order")
	public ResponseEntity<Order> order(@PathVariable Long productId) {
		Order order = service.order(productId);
		order.getUser().setApiKey(Base64.getEncoder().encodeToString(order.getUser().getApiKey().getBytes()));
		return ResponseEntity.ok(order);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody OrderStatusUpdate dto) {
		service.updateStatus(id, dto.getStatus());
		return ResponseEntity.noContent().build();
	}

}
