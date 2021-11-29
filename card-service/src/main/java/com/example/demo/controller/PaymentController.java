package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.mapper.OrderMapper;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping(value = "/card")
public class PaymentController {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderService orderService;

}