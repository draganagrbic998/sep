package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.PaymentRequestDTO;
import com.example.demo.mapper.PaymentRequestMapper;
import com.example.demo.model.PaymentRequest;
import com.example.demo.service.PaymentRequestService;

@RestController
@RequestMapping("/payment-requests")
public class PaymentRequestController {

	@Autowired
	private PaymentRequestMapper paymentRequestMapper;

	@Autowired
	private PaymentRequestService paymentRequestService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	private ResponseEntity<?> createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
		PaymentRequest paymentRequest = paymentRequestMapper.toEntity(paymentRequestDTO);
		return new ResponseEntity<>(paymentRequestMapper.toDTO(paymentRequestService.save(paymentRequest)),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/confirm/{paymentRequestId}", method = RequestMethod.POST)
	private String confirmPayment(@RequestBody ClientDTO clientDTO, @PathVariable Integer paymentRequestId)
			throws NotFoundException {
		return paymentRequestService.confirmPaymentRequest(clientDTO, paymentRequestId);
	}

}