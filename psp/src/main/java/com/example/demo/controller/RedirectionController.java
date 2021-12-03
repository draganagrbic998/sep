package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.OrderCreateDTO;
import com.example.demo.dto.OrderCreatedDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/redirection")
public class RedirectionController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private OrderService orderService;

	@PostMapping("/{method}/{orderIdWebshop}")
	public ResponseEntity<?> createOrderInPaymentService(@PathVariable String method,
			@PathVariable Integer orderIdWebshop, @RequestBody OrderCreateDTO orderCreateDTO) throws NotFoundException {
		log.info("RedirectionController - createOrderInPaymentService: method=" + method + " orderIdWebshop="
				+ orderIdWebshop);
		Order order = orderService.findById(orderIdWebshop);

		// Kupac mora u roku od 5 minuta da odabere nacin placanja
		if (order.getTicks() >= 5) {
			log.error("Order: id=" + order.getId() + " exceeded maximum tick count.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		log.info("createOrderInPaymentService - sending order to payment service: name=" + method);
		ResponseEntity<OrderCreatedDTO> responseEntity = restTemplate.exchange("http://localhost:8762/" + method,
				HttpMethod.POST, new HttpEntity<OrderCreateDTO>(orderCreateDTO), OrderCreatedDTO.class);

		log.info("createOrderInPaymentService - OrderCreatedDTO: status=" + responseEntity.getBody().getStatus());

		order.setStatus(OrderStatus.SENT);
		orderService.save(order);

		return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
	}
}
