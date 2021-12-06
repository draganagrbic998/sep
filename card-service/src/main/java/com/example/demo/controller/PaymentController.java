package com.example.demo.controller;

import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.PaymentRequestCompleted;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping
@Log4j2
public class PaymentController {

	private final OrderService orderService;

	@PostMapping
	public Order create(@RequestBody Order dto) {
		log.info("PaymentController - create");
		dto.setMerchantApiKey(new String(Base64.getDecoder().decode(dto.getMerchantApiKey())));
		return orderService.save(dto);
	}

	@PostMapping("/complete")
	public ResponseEntity<String> complete(@RequestBody PaymentRequestCompleted dto) {
		log.info("PaymentController - completePayment: orderId=" + dto.getId());
		return ResponseEntity.ok(orderService.complete(dto));
	}

	@GetMapping("/pay/{merchantApiKey}/{orderId}")
	public ModelAndView pay(@PathVariable String merchantApiKey, @PathVariable Long orderId) {
		// log.info("PaymentController - pay: merchantApiKey=" + merchantApiKeyTemp + "
		// orderId=" + orderId);
		return new ModelAndView(
				"redirect:" + orderService.pay(orderId, new String(Base64.getDecoder().decode(merchantApiKey))));
	}

}