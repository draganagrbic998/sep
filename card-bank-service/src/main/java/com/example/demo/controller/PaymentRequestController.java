package com.example.demo.controller;

import javax.validation.Valid;

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

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/payment-requests")
@Log4j2
public class PaymentRequestController {

	private final PaymentRequestService service;

	@PostMapping
	public ResponseEntity<PaymentRequestResponse> create(@Valid @RequestBody PaymentRequest dto) {
		log.info("PaymentRequestController - create");
		return ResponseEntity.ok(new PaymentRequestResponse(service.save(dto)));
	}

	@PostMapping("/confirm/{id}")
	public ResponseEntity<String> confirmPayment(@PathVariable Long id, @Valid @RequestBody ClientDTO dto) {
		log.info("PaymentRequestController - confirmPayment: paymentRequestId=" + id);
		return ResponseEntity.ok(service.confirm(id, dto));
	}

}