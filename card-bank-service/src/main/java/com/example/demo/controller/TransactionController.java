package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.TransactionService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/transactions")
@Log4j2
public class TransactionController {

	private final TransactionService service;

	@GetMapping("/{orderId}")
	private ResponseEntity<String> getTransaction(@PathVariable Long orderId) {
		log.info("TransactionController - getTransaction: orderId=" + orderId);
		return ResponseEntity.ok(service.findByMerchantOrderId(orderId).getStatus().toString());
	}

}
