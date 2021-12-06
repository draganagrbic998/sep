package com.example.demo.controller;

import java.util.Base64;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentMethodService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final OrderService orderService;
	private final PaymentMethodService paymentMethodService;

	@GetMapping("/selectPaymentMethod/{orderId}")
	public String selectPaymentMethod(@PathVariable Long orderId, Model model) {

		Order order = orderService.readOne(orderId);
		model.addAttribute("paymentMethods", paymentMethodService
				.getPaymentMethods(new String(Base64.getDecoder().decode(order.getMerchantApiKey()))));

		// model.addAttribute("payUrl", asd);
		// model.addAttribute("zuulGatewayUrl", asd);
		model.addAttribute("merchantApiKey", order.getMerchantApiKey());
		model.addAttribute("price", order.getPrice());
		model.addAttribute("currency", order.getCurrency());
		model.addAttribute("callbackUrl", order.getCallbackUrl());

		return "selectPaymentMethod";
	}
}