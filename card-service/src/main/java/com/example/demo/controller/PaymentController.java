package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.OrderCreatedDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.PaymentRequestCompletedDTO;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(value = "/card")
public class PaymentController {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderService orderService;

	@PostMapping(value = "/create/payment")
	public OrderCreatedDTO createPayment(@RequestBody OrderDTO orderDTO) {
		log.info("PaymentController - createPayment");
		Order order = orderMapper.toEntity(orderDTO);
		return orderMapper.toDTO(orderService.save(order));
	}

	@RequestMapping(value = "/pay/{merchantApiKey}/{orderId}", method = RequestMethod.GET)
	public ModelAndView pay(@PathVariable String merchantApiKey, @PathVariable Integer orderId)
			throws NotFoundException {
		log.info("PaymentController - pay: merchantApiKey=" + merchantApiKey + " orderId=" + orderId.toString());
		String redirectUrl = orderService.pay(orderId, merchantApiKey);
		return new ModelAndView("redirect:" + redirectUrl);
	}

	@RequestMapping(value = "/complete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> completePayment(@RequestBody PaymentRequestCompletedDTO paymentRequestCompletedDTO)
			throws NotFoundException {
		log.info("PaymentController - completePayment: orderId=" + paymentRequestCompletedDTO.getId().toString());
		String payment = orderService.completePayment(paymentRequestCompletedDTO);
		return ResponseEntity.ok(payment);
	}

}