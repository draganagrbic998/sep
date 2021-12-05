package com.example.demo.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Order;
import com.example.demo.model.PaymentMethod;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentMethodService;
import com.example.demo.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final UserService userService;
	private final OrderService orderService;
	private final PaymentMethodService paymentMethodService;

	@GetMapping("/selectPaymentMethod/{merchantApiKey}/{orderWebshopId}")
	public String selectPaymentMethod(@PathVariable String merchantApiKey, @PathVariable Long orderWebshopId,
			Model model) {
		merchantApiKey = new String(Base64.getDecoder().decode(merchantApiKey));
		List<PaymentMethod> paymentMethods = paymentMethodService.getPaymentMethods(merchantApiKey);
		User merchant = userService.findByApiKey(merchantApiKey);
		Order order = orderService.readOne(orderWebshopId);

		model.addAttribute("orderIdPSP", order.getId());
		model.addAttribute("orderIdWebShop", order.getWebshopId());
		model.addAttribute("orderPrice", order.getPrice());
		model.addAttribute("orderCurrency", order.getCurrency());
		model.addAttribute("callbackUrl", order.getCallbackUrl());

		model.addAttribute("paymentMethods", paymentMethods);
		model.addAttribute("merchantApiKey", merchant.getApiKey());

		return "selectPaymentMethod";
	}
}