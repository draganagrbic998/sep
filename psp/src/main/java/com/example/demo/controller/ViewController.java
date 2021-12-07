package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentMethodService;
import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final OrderService orderService;
	private final PaymentMethodService paymentMethodService;
	private final PropertiesData properties;

	@GetMapping("/selectPaymentMethod/{orderId}")
	public String selectPaymentMethod(@PathVariable Long orderId, Model model) {
		Order order = orderService.readOne(orderId);

		model.addAttribute("zuulGatewayUrl", properties.zuulGatewayUrl);
		model.addAttribute("payUrl", properties.payUrl);
		model.addAttribute("merchantApiKey", order.getMerchantApiKey());
		model.addAttribute("price", order.getPrice());
		model.addAttribute("currency", order.getCurrency());
		model.addAttribute("callbackUrl", order.getCallbackUrl());
		model.addAttribute("paymentMethods", paymentMethodService.getPaymentMethods(order.getMerchantApiKey()));

		return "selectPaymentMethod";
	}
}
