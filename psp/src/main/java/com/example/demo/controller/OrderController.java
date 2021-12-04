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
@Log4j2
@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderMapper mapper;
	private final OrderService service;

	@PostMapping
	private Order create(@RequestBody OrderWebshopDTO orderWebshopDTO) {
		log.info("OrderController - create");
		Order o = mapper.toEntity(orderWebshopDTO);
		o.setStatus(OrderStatus.CREATED);
		Order savedOrder = service.save(o);
		return savedOrder;
	}

}
