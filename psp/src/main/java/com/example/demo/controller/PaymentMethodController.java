package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.PaymentMethodDTO;
import com.example.demo.mapper.PaymentMethodMapper;
import com.example.demo.model.PaymentMethod;
import com.example.demo.service.PaymentMethodService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/payment-methods")
public class PaymentMethodController {

	@Autowired
	private PaymentMethodMapper paymentMethodMapper;

	@Autowired
	private PaymentMethodService paymentMethodService;

	@PostMapping
	private ResponseEntity<PaymentMethod> create(@RequestBody PaymentMethodDTO dto) {
		log.info("PaymentMethodController - create");
		PaymentMethod m = paymentMethodMapper.toEntity(dto);
		return new ResponseEntity<>(paymentMethodService.save(m), HttpStatus.CREATED);
	}

	@DeleteMapping("/{paymentMethodId}")
	public ResponseEntity<?> remove(@PathVariable Integer paymentMethodId) throws NotFoundException {
		log.info("PaymentMethodController - remove: id=" + paymentMethodId);
		return new ResponseEntity<>(paymentMethodService.remove(paymentMethodId), HttpStatus.NO_CONTENT);
	}

}
