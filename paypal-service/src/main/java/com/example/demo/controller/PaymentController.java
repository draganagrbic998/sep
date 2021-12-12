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
import com.paypal.base.rest.PayPalRESTException;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@Log4j2
public class PaymentController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Order> create(@RequestBody Order dto) {
		log.info("PaymentController - create");
		return ResponseEntity.ok(orderService.createPayment(orderService.save(dto)));
	}

	@GetMapping("/pay/{orderId}")
	public ModelAndView pay(@PathVariable Long orderId) {
		log.info("PaymentController - pay: orderId=" + orderId);
		String redirectUrl = "https://localhost:8086/view/paypal_payment/" + orderId;
		return new ModelAndView("redirect:" + redirectUrl);
	}

	@GetMapping(value = "/complete/payment/{paymentId}/{payerId}")
	public ResponseEntity<String> complete(@PathVariable String paymentId, @PathVariable String payerId) {
		log.info("PaymentController - complete");
		return ResponseEntity.ok(orderService.completePayment(paymentId, payerId));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<String> getOrderForPaypal(@PathVariable Long orderId)
			throws PayPalRESTException {
		log.info("PaymentController - getOrderForPaypal: orderId=" + orderId);
		return ResponseEntity.ok(orderService.getOrderDetails(orderId));
	}
}