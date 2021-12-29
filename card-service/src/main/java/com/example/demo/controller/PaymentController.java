package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.OrderStatusUpdate;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class PaymentController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Order> create(@RequestBody Order dto) {
		dto.setId(null);
		return ResponseEntity.ok(orderService.save(dto));
	}

	@PutMapping("/{orderId}")
	public ResponseEntity<Order> complete(@PathVariable Long orderId, @RequestBody OrderStatusUpdate dto) {
		return ResponseEntity.ok(orderService.complete(orderId, dto.getStatus()));
	}

	@GetMapping("/pay/{orderId}")
	public ModelAndView pay(@PathVariable Long orderId) {
		return new ModelAndView("redirect:" + orderService.pay(orderId));
	}

}