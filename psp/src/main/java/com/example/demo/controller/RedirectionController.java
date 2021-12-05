package com.example.demo.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.OrderCreateDTO;
import com.example.demo.dto.OrderCreatedDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/redirection")
@Log4j2
public class RedirectionController {

	private final RestTemplate restTemplate;
	private final OrderService orderService;

	@PostMapping("/{method}/{orderIdWebshop}")
	public ResponseEntity<OrderCreatedDTO> createOrderInPaymentService(@PathVariable String method,
			@PathVariable Long orderIdWebshop, @RequestBody OrderCreateDTO orderCreateDTO) {
		log.info("RedirectionController - createOrderInPaymentService: method=" + method + " orderIdWebshop="
				+ orderIdWebshop);
		Order order = orderService.readOne(orderIdWebshop);

		if (order.getTicks() >= 5) {
			log.error("Order: id=" + order.getId() + " exceeded maximum tick count.");
			return ResponseEntity.badRequest().build();
		}

		log.info("createOrderInPaymentService - sending order to payment service: name=" + method);
		ResponseEntity<OrderCreatedDTO> responseEntity = restTemplate.exchange("http://localhost:8082/" + method,
				HttpMethod.POST, new HttpEntity<OrderCreateDTO>(orderCreateDTO), OrderCreatedDTO.class);

		log.info("createOrderInPaymentService - OrderCreatedDTO: status=" + responseEntity.getBody().getStatus());

		order.setStatus(OrderStatus.SENT);
		orderService.save(order);

		return ResponseEntity.ok(responseEntity.getBody());
	}
}
