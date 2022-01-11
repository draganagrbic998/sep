package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.OrderService;
import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final OrderService orderService;
	private final PropertiesData properties;

	@RequestMapping("/bitcoin_payment/{orderId}")
	public String bitcoinPayment(@PathVariable Long orderId, Model model) {
		model.addAttribute("orderPaymentUrl", orderService.findOne(orderId).getPaymentUrl());
		return "confirmOrder";
	}

	@RequestMapping("/register")
	public String register(Model model) {
		model.addAttribute("registerUrl", properties.registerUrl);
		return "register";
	}

	@RequestMapping("/success_url")
	public String success() {
		return "success";
	}

	@RequestMapping("/cancel_url")
	public String cancel() {
		return "cancel";
	}

	@RequestMapping("/error_url")
	public String error() {
		return "error";
	}

}
