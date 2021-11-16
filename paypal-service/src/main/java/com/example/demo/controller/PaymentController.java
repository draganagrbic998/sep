package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.PaymentService;
import com.example.demo.utils.PaymentParams;

import java.util.Map;

@RestController
@RequestMapping(value = "/paypal-service")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/make/payment")
	public Map<String, Object> makePayment(@RequestParam("sum") String sum) {
		return paymentService.createPayment(sum);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/complete/payment")
	public Map<String, Object> completePayment(@RequestBody PaymentParams params) {
		return paymentService.completePayment(params);
	}

}