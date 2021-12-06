package com.example.demo.controller;

import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.PaymentRequestCompleted;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class PaymentController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Order> create(@RequestBody Order dto) {
		dto.setMerchantApiKey(new String(Base64.getDecoder().decode(dto.getMerchantApiKey())));
		return ResponseEntity.ok(orderService.save(dto));
	}

	@PostMapping("/complete/{orderId}")
	public ResponseEntity<String> complete(@PathVariable Long id, @RequestBody PaymentRequestCompleted dto) {
		return ResponseEntity.ok(orderService.complete(id, dto));
	}

	@GetMapping("/pay/{orderId}")
	public ModelAndView pay(@PathVariable Long orderId) {
		return new ModelAndView("redirect:" + orderService.pay(orderId));
	}

}