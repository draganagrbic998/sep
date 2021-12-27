package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	@Autowired
	OrderService orderService;

	@RequestMapping("/bitcoin_payment/{orderId}")
	public String paypalPayment(@PathVariable Long orderId, Model model) {
		model.addAttribute("orderPaymentUrl", orderService.findOne(orderId).getPaymentUrl());
		return "confirmOrder";
	}

	@RequestMapping("/register")
	public String register(Model model) {
		return "register";
	}

	@RequestMapping("/success_url")
	public String success(Model model) {
		return "success";
	}

	@RequestMapping("/cancel_url")
	public String cancel(Model model) {
		return "cancel";
	}

	@RequestMapping("/error_url")
	public String error(Model model) {
		return "error";
	}

}
