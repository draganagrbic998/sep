package com.example.demo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.OrderCreateDTO;
import com.example.demo.dto.OrderCreatedDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/redirection")
public class RedirectionController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/{method}/{orderIdWebshop}", method = RequestMethod.POST)
	public ResponseEntity<?> createOrderInPaymentService(@PathVariable String method, @PathVariable UUID orderIdWebshop,
			@RequestBody OrderCreateDTO orderCreateDTO) throws NotFoundException {
		Order order = orderService.findById(orderIdWebshop);

		ResponseEntity<OrderCreatedDTO> responseEntity = restTemplate.exchange(
				"http://localhost:8762/" + method + "/create/payment", HttpMethod.POST,
				new HttpEntity<OrderCreateDTO>(orderCreateDTO), OrderCreatedDTO.class);

		order.setStatus(OrderStatus.SENT);
		orderService.save(order);

		return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
	}
}
