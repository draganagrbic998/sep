package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@Log4j2
public class OrderController {

	private final OrderService service;
	private final PropertiesData properties;

	@PostMapping
	public ResponseEntity<Order> create(@RequestBody Order dto) {
		log.info("OrderController - create");
		return ResponseEntity.ok(service.save(dto));
	}

	@GetMapping("/pay/{orderId}")
	public ModelAndView pay(@PathVariable Long orderId) {
		log.info("OrderController - pay");
		return new ModelAndView(
				"redirect:" + properties.bitcoinPaymentUrl + "/" + service.createPayment(orderId).getId());
	}

	@PostMapping("/complete_payment/{orderId}")
	public ResponseEntity<String> completePayment(@PathVariable Long orderId) {
		log.info("OrderController - completePayment");
		return ResponseEntity.ok(service.completePayment(orderId));
	}
}
