package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

@Controller
@RequestMapping("/view")
public class ViewController {

	@Autowired
	OrderService orderService;

	@RequestMapping(value = "/paypal_payment/{orderId}")
	public String paypal(@PathVariable Integer orderId, Model model) {
		Order order = orderService.getOrder(orderId);
		model.addAttribute("orderId", orderId);
		model.addAttribute("merchantId", order.getMerchantId());
		model.addAttribute("payPalOrderId", order.getPayPalOrderId());
		return "order";
	}

	@RequestMapping(value = "/register/{merchantId}")
	public String register(@PathVariable String merchantId, Model model) {
		model.addAttribute("merchantId", merchantId);
		return "register";
	}

	@RequestMapping(value = "/success_url")
	public String successPayment(@RequestParam("orderId") Integer orderId, Model model) {
		Order order = orderService.getOrder(orderId);
		model.addAttribute("redirect", order.getCallbackUrl());
		return "success";
	}

	@RequestMapping(value = "/cancel_url")
	public String cancelPayment(@RequestParam("orderId") Integer orderId, Model model) {
		Order order = orderService.getOrder(orderId);
		model.addAttribute("redirect", order.getCallbackUrl());
		return "cancel";
	}

	@RequestMapping(value = "/error_url")
	public String errorPayment(@RequestParam("orderId") Integer orderId, Model model) {
		Order order = orderService.getOrder(orderId);
		model.addAttribute("redirect", order.getCallbackUrl());
		return "error";
	}

}