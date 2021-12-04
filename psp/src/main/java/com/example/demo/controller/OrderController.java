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

@Log4j2
@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

	private final OrderMapper orderMapper;
	private final OrderService orderService;

	@PostMapping
	private Order create(@RequestBody OrderWebshopDTO orderWebshopDTO) {
		log.info("OrderController - create");
		Order o = orderMapper.toEntity(orderWebshopDTO);
		o.setStatus(OrderStatus.CREATED);
		Order savedOrder = orderService.save(o);
		return savedOrder;
	}

}
