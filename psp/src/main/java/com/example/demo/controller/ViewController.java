package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.model.Order;
import com.example.demo.model.PaymentMethod;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentMethodService;
import com.example.demo.service.UserService;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final UserService userService;
	private final OrderService orderService;
	private final PaymentMethodService paymentMethodService;
	private final DatabaseCipher cipher;

	@RequestMapping(value = "/selectPaymentMethod/{merchantApiKey}/{orderId}", method = RequestMethod.GET)
	public String selectPaymentMethod(@PathVariable UUID merchantApiKey, @PathVariable Long orderId, Model model) {
		List<PaymentMethod> paymentMethods = paymentMethodService.getPaymentMethods(merchantApiKey);

		User merchant = userService.findByApiKey(merchantApiKey.toString());
		Order order = orderService.readOne(orderId);

		model.addAttribute("orderIdPSP", order.getId());
		model.addAttribute("orderIdWebShop", order.getWebshopId());
		model.addAttribute("orderPrice", order.getPrice());
		model.addAttribute("orderCurrency", order.getCurrency());
		model.addAttribute("callbackUrl", order.getCallbackUrl());

		model.addAttribute("paymentMethods", paymentMethods);
		model.addAttribute("merchantApiKey", cipher.decrypt(merchant.getApiKey()));

		return "selectPaymentMethod";
	}
}