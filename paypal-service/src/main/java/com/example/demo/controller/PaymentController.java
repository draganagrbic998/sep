package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.OrderDTO;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import com.example.demo.utils.PaymentParams;

import java.util.Map;

// Dok ne napravim psp ovo ostaje ovako. svakako nema smisla da testiram da li sve radi dok psp ne napravim
@RestController
@RequestMapping(value = "/paypal-service")
public class PaymentController {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderService orderService;

	@PostMapping(value = "/make/payment")
	public Map<String, Object> createPayment(@RequestBody OrderDTO orderDTO) {
		Order order = orderMapper.toEntity(orderDTO);
		return orderService.createPayment(order);
	}

	@PostMapping(value = "/complete/payment")
	public Map<String, Object> completePayment(@RequestBody PaymentParams params) {
		return orderService.completePayment(params);
	}

}