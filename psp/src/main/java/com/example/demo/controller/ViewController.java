package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.PaymentMethod;
import com.example.demo.service.MerchantService;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentMethodService;

@Controller
public class ViewController {

	@Autowired
	private PaymentMethodService paymentMethodService;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/selectPaymentMethod/{appApiKey}/{orderId}", method = RequestMethod.GET)
	public String selectPaymentMethod(@PathVariable UUID appApiKey, @PathVariable UUID orderId, Model model)
			throws NotFoundException {
		List<PaymentMethod> paymentMethods = paymentMethodService.getPaymentMethods(appApiKey);

		String appApiKeyString = appApiKey.toString();
		Merchant merchant = merchantService.findByApiKey(appApiKeyString);

		Order o = orderService.findById(orderId);

		model.addAttribute("orderIdPSP", o.getId());
		model.addAttribute("orderId", o.getOrderIdWebshop());
		model.addAttribute("orderPrice", o.getPrice());
		model.addAttribute("orderCurrency", o.getCurrency());
		model.addAttribute("callbackUrl", o.getCallbackUrl());

		model.addAttribute("paymentMethods", paymentMethods);
		model.addAttribute("merchantId", merchant.getId());

		return "selectPaymentMethod";
	}
}