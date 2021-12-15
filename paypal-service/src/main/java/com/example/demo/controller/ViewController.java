package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final DatabaseCipher cipher;
	private final OrderService orderService;

	@RequestMapping("/choose_type/{orderId}")
	public String chooseType(@PathVariable Long orderId, Model model) {
		model.addAttribute("orderId", orderId);
		return "chooseType";
	}

	@RequestMapping("/paypal_payment/{orderId}")
	public String paypal(@PathVariable Long orderId, Model model) {
		Order order = orderService.findById(orderId);
		model.addAttribute("orderId", orderId);
		model.addAttribute("payPalOrderId", cipher.decrypt(order.getPayPalOrderId()));
		return "confirmOrder";
	}

	@RequestMapping("/register")
	public String register(Model model) {
		return "register";
	}

	@RequestMapping("/success_url")
	public String successPayment(Model model) {
		return "success";
	}

	@RequestMapping("/cancel_url")
	public String cancelPayment(Model model) {
		return "cancel";
	}

	@RequestMapping("/error_url")
	public String errorPayment(Model model) {
		return "error";
	}

}
