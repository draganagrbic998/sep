package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.OrderService;

@Controller
@RequestMapping("/view")
public class ViewController {

	@Autowired
	OrderService orderService;

}
