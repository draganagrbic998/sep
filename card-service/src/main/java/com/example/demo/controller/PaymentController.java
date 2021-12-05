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
import com.example.demo.dto.PaymentRequestCompletedDTO;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/card")
@Log4j2
public class PaymentController {

	private final OrderMapper orderMapper;
	private final OrderService orderService;

	@PostMapping
	public OrderCreatedDTO create(@RequestBody OrderDTO dto) {
		log.info("PaymentController - create");
		return orderMapper.toDTO(orderService.save(orderMapper.toEntity(dto)));
	}

	@GetMapping("/pay/{merchantApiKey}/{orderId}")
	public ModelAndView pay(@PathVariable String merchantApiKey, @PathVariable Long orderId) {
		log.info("PaymentController - pay: merchantApiKey=" + merchantApiKey + " orderId=" + orderId);
		return new ModelAndView("redirect:" + orderService.pay(orderId, merchantApiKey));
	}

	@PostMapping("/complete")
	public ResponseEntity<String> completePayment(@RequestBody PaymentRequestCompletedDTO dto) {
		log.info("PaymentController - completePayment: orderId=" + dto.getId());
		return ResponseEntity.ok(orderService.completePayment(dto));
	}

}