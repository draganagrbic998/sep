package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.OrderCreatedDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.service.OrderService;
import com.paypal.base.rest.PayPalRESTException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(value = "/paypal")
public class PaymentController {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderService orderService;

	@PostMapping(value = "/create/payment")
	public OrderCreatedDTO create(@RequestBody OrderDTO orderDTO) {
		log.info("PaymentController - create");
		return orderMapper.toDTO(orderService.createPayment(orderMapper.toEntity(orderDTO)));
	}

	@RequestMapping(value = "/pay/{merchantApiKey}/{orderId}", method = RequestMethod.GET)
	public ModelAndView pay(@PathVariable Integer merchantApiKey, @PathVariable Integer orderId) {
		log.info("PaymentController - pay: merchantApiKey" + merchantApiKey + " orderId=" + orderId);
		String redirectUrl = "http://localhost:8086/view/paypal_payment/" + orderId.toString();
		return new ModelAndView("redirect:" + redirectUrl);
	}

	@PostMapping(value = "/complete/payment/{paymentId}/{payerId}")
	public ResponseEntity<String> complete(@PathVariable String paymentId, @PathVariable String payerId) {
		log.info("PaymentController - complete");
		String payment = orderService.completePayment(paymentId, payerId);
		return ResponseEntity.ok(payment);
	}

	@RequestMapping(value = "/getOrder/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<String> getOrderForPaypal(@PathVariable Integer orderId) throws PayPalRESTException {
		log.info("PaymentController - getOrderForPaypal: orderId=" + orderId);
		String payment = orderService.getOrderDetails(orderId);
		return ResponseEntity.ok(payment);
	}
}