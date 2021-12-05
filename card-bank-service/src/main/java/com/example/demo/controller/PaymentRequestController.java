package com.example.demo.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.PaymentRequestDTO;
import com.example.demo.dto.PaymentRequestResponseDTO;
import com.example.demo.mapper.PaymentRequestMapper;
import com.example.demo.service.PaymentRequestService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/payment-requests")
@Log4j2
public class PaymentRequestController {

	private final PaymentRequestMapper mapper;
	private final PaymentRequestService service;

	@PostMapping
	private ResponseEntity<PaymentRequestResponseDTO> create(@RequestBody PaymentRequestDTO dto) {
		log.info("PaymentRequestController - create");
		return ResponseEntity.ok(mapper.toDTO(service.save(mapper.toEntity(dto))));
	}

	@PostMapping("/confirm/{id}")
	private ResponseEntity<String> confirmPayment(@PathVariable Long id, @RequestBody ClientDTO dto)
			throws IOException {
		log.info("PaymentRequestController - confirmPayment: paymentRequestId=" + id);
		return ResponseEntity.ok(service.confirmPaymentRequest(id, dto));
	}

}