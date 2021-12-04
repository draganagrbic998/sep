package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderWebshopDTO;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
@Log4j2
public class OrderController {

	private final OrderMapper mapper;
	private final OrderService service;

	@PostMapping
	private Order create(@RequestBody OrderWebshopDTO dto) {
		log.info("OrderController - create");
		Order order = mapper.toEntity(dto);
		order.setStatus(OrderStatus.CREATED);
		order = service.save(order);
		return order;
	}

}
