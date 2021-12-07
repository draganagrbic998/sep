package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService service;

	@PostMapping
	public ResponseEntity<Order> create(@RequestBody Order dto) {
		return ResponseEntity.ok(service.save(dto));
	}

}
