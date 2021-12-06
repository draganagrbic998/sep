package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.PaymentRequestResponse;
import com.example.demo.model.PaymentRequest;
import com.example.demo.service.PaymentRequestService;
import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/payment-requests")
public class PaymentRequestController {

	private final PaymentRequestService service;
	private final PropertiesData properties;

	@PostMapping
	public ResponseEntity<PaymentRequestResponse> create(@RequestBody PaymentRequest dto) {
		return ResponseEntity.ok(new PaymentRequestResponse(service.save(dto), properties.paymentView));
	}

	@PostMapping("/confirm/{id}")
	public ResponseEntity<String> confirm(@PathVariable Long id, @RequestBody ClientDTO dto) {
		return ResponseEntity.ok(service.confirm(id, dto));
	}

}