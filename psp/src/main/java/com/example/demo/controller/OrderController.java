package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderWebshopDTO;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	private Order create(@RequestBody OrderWebshopDTO orderWebshopDTO) {
		log.info("OrderController - create");
		Order o = orderMapper.toEntity(orderWebshopDTO);
		o.setStatus(OrderStatus.CREATED);
		Order savedOrder = orderService.save(o);
		return savedOrder;
	}

}
