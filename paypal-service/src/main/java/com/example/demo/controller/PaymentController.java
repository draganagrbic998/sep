package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.OrderCreatedDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.service.OrderService;
import com.paypal.base.rest.PayPalRESTException;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping
@Log4j2
public class PaymentController {

	private final OrderMapper orderMapper;
	private final OrderService orderService;

	@PostMapping
	public OrderCreatedDTO create(@RequestBody OrderDTO dto) {
		log.info("PaymentController - create");
		return orderMapper.toDTO(orderService.createPayment(orderMapper.toEntity(dto)));
	}

	@GetMapping("/pay/{merchantApiKey}/{orderId}")
	public ModelAndView pay(@PathVariable Long merchantApiKey, @PathVariable Long orderId) {
		log.info("PaymentController - pay: merchantApiKey" + merchantApiKey + " orderId=" + orderId);
		String redirectUrl = "http://localhost:8086/view/paypal_payment/" + orderId.toString();
		return new ModelAndView("redirect:" + redirectUrl);
	}

	@PostMapping(value = "/complete/payment/{paymentId}/{payerId}")
	public ResponseEntity<String> complete(@PathVariable String paymentId, @PathVariable String payerId) {
		log.info("PaymentController - complete");
		return ResponseEntity.ok(orderService.completePayment(paymentId, payerId));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<String> getOrderForPaypal(@PathVariable Long orderId) throws PayPalRESTException {
		log.info("PaymentController - getOrderForPaypal: orderId=" + orderId);
		return ResponseEntity.ok(orderService.getOrderDetails(orderId));
	}
}