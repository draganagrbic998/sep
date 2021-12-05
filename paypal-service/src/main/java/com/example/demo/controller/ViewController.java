package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final OrderService orderService;

	@RequestMapping(value = "/paypal_payment/{orderId}")
	public String paypal(@PathVariable Long orderId, Model model) {
		Order order = orderService.findById(orderId);
		model.addAttribute("orderId", orderId);
		model.addAttribute("payPalOrderId", order.getPayPalOrderId());
		return "confirmOrder";
	}

	@RequestMapping(value = "/register")
	public String register(Model model) {
		return "register";
	}

	@RequestMapping(value = "/success_url")
	public String successPayment(@RequestParam("orderId") Long orderId, Model model) {
		Order order = orderService.findById(orderId);
		model.addAttribute("redirect", order.getCallbackUrl());
		return "success";
	}

	@RequestMapping(value = "/cancel_url")
	public String cancelPayment(@RequestParam("orderId") Long orderId, Model model) {
		Order order = orderService.findById(orderId);
		model.addAttribute("redirect", order.getCallbackUrl());
		return "cancel";
	}

	@RequestMapping(value = "/error_url")
	public String errorPayment(@RequestParam("orderId") Long orderId, Model model) {
		Order order = orderService.findById(orderId);
		model.addAttribute("redirect", order.getCallbackUrl());
		return "error";
	}

}
