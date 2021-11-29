package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.OrderDTO;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import com.paypal.base.rest.PayPalRESTException;

import java.util.Map;

// Dok ne napravim psp ovo ostaje ovako. svakako nema smisla da testiram da li sve radi dok psp ne napravim
@RestController
@RequestMapping(value = "/paypal")
public class PaymentController {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderService orderService;

	@PostMapping(value = "/create/payment")
	public Map<String, Object> createPayment(@RequestBody OrderDTO orderDTO) {
		Order order = orderMapper.toEntity(orderDTO);
		return orderService.createPayment(order);
	}

	@RequestMapping(value = "/pay/{merchantId}/{orderId}", method = RequestMethod.GET)
	public ModelAndView pay(@PathVariable Integer merchantId, @PathVariable Integer orderId) {
		String redirectUrl = "http://localhost:8086/view/paypal_payment/" + orderId;
		return new ModelAndView("redirect:" + redirectUrl);
	}

	@PostMapping(value = "/complete/payment/{paymentId}/{payerId}")
	public ResponseEntity<String> completePayment(@PathVariable String paymentId, @PathVariable String payerId) {
		String payment = orderService.completePayment(paymentId, payerId);
		return ResponseEntity.ok(payment);
	}

	@RequestMapping(value = "/getOrder/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<String> createOrderForPaypal(@PathVariable Integer orderId) throws PayPalRESTException {
		String payment = orderService.getOrderDetails(orderId);
		return ResponseEntity.ok(payment);
	}
}