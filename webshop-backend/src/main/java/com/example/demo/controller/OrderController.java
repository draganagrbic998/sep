package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
@Log4j2
public class OrderController {

	private final OrderService service;

	@GetMapping
	public ResponseEntity<List<Order>> read() {
		log.info("OrderController - read");
		return ResponseEntity.ok(service.read());
	}

}
