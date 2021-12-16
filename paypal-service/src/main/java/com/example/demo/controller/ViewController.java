package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.Subscription;
import com.example.demo.service.MerchantService;
import com.example.demo.service.OrderService;
import com.example.demo.service.SubscriptionService;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final DatabaseCipher cipher;
	private final SubscriptionService subscriptionService;
	private final OrderService orderService;
	private final MerchantService merchantService;

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

	@RequestMapping("/create_plan/{orderId}")
	public String plan(@PathVariable Long orderId, Model model) {
		model.addAttribute("orderId", orderId);
		return "createPlan";
	}

	@RequestMapping("/subscription_payment/{subscriptionId}")
	public String subscriptionPayment(@PathVariable Long subscriptionId, Model model) {
		Subscription subscription = subscriptionService.findById(subscriptionId);
		Order order = orderService.findById(subscription.getOrderId());
		Merchant merchant = cipher.decrypt(merchantService.findOneByApiKey(order.getMerchantApiKey()));

		model.addAttribute("clientId", merchant.getClientId());
		model.addAttribute("subscriptionId", subscriptionId);

		merchant = cipher.encrypt(merchant);
		return "confirmSubscription";
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

	@RequestMapping("/subscription_success_url")
	public String subscriptionSuccess(Model model) {
		return "subscriptionSuccess";
	}

	@RequestMapping("/subscription_cancel_url")
	public String subscriptionCancel(Model model) {
		return "subscriptionCancel";
	}

	@RequestMapping("/subscription_error_url")
	public String subscriptionError(Model model) {
		return "subscriptionError";
	}

	@RequestMapping("/error_url")
	public String errorPayment(Model model) {
		return "error";
	}

}
