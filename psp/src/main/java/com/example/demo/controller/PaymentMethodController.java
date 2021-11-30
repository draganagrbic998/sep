package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.PaymentMethodDTO;
import com.example.demo.mapper.PaymentMethodMapper;
import com.example.demo.model.PaymentMethod;
import com.example.demo.service.PaymentMethodService;

@RestController
@RequestMapping("/payment-methods")
public class PaymentMethodController {

	@Autowired
	private PaymentMethodMapper paymentMethodMapper;

	@Autowired
	private PaymentMethodService paymentMethodService;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<PaymentMethod> createPaymentMethod(@RequestBody PaymentMethodDTO dto) {
		PaymentMethod m = paymentMethodMapper.toEntity(dto);
		return new ResponseEntity<>(paymentMethodService.save(m), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{paymentMethodId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removePaymentMethod(@PathVariable Integer paymentMethodId) throws NotFoundException {
		return new ResponseEntity<>(paymentMethodService.remove(paymentMethodId), HttpStatus.NO_CONTENT);
	}

}
