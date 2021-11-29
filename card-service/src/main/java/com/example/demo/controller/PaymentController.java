package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.PaymentRequestCompletedDTO;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping(value = "/card")
public class PaymentController {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderService orderService;

	@PostMapping(value = "/create/payment")
	public Order createPayment(@RequestBody OrderDTO orderDTO) {
		Order order = orderMapper.toEntity(orderDTO);
		return orderService.save(order);
	}

	@RequestMapping(value = "/pay/{merchantWebshopId}/{orderId}", method = RequestMethod.GET)
	public ModelAndView pay(@PathVariable Integer merchantWebshopId, @PathVariable Integer orderId)
			throws NotFoundException {
		String redirectUrl = orderService.pay(orderId, merchantWebshopId);
		return new ModelAndView("redirect:" + redirectUrl);
	}

	@RequestMapping(value = "/complete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> completePayment(@RequestBody PaymentRequestCompletedDTO paymentRequestCompletedDTO)
			throws NotFoundException {
		String payment = orderService.completePayment(paymentRequestCompletedDTO);
		return ResponseEntity.ok(payment);
	}

}