package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final MerchantRepository merchantRepo;
	private final OrderRepository orderRepo;
	private final SubscriptionRepository subscriptionRepo;
	private final DatabaseCipher cipher;

	@RequestMapping("/paypal_payment/{orderId}")
	public String paypalPayment(@PathVariable Long orderId, Model model) {
		Order order = cipher.decrypt(orderRepo.findById(orderId).get());
		model.addAttribute("orderId", orderId);
		model.addAttribute("payPalOrderId", order.getPayPalOrderId());
		return "confirmOrder";
	}

	@RequestMapping("/subscription_payment/{subscriptionId}")
	public String subscriptionPayment(@PathVariable Long subscriptionId, Model model) {
		Merchant merchant = cipher.decrypt(merchantRepo.findByMerchantApiKey(orderRepo
				.findById(subscriptionRepo.findById(subscriptionId).get().getOrderId()).get().getMerchantApiKey()));

		model.addAttribute("subscriptionId", subscriptionId);
		model.addAttribute("clientId", merchant.getClientId());
		return "confirmSubscription";
	}

	@RequestMapping("/choose_type/{orderId}")
	public String chooseType(@PathVariable Long orderId, Model model) {
		model.addAttribute("orderId", orderId);
		return "chooseType";
	}

	@RequestMapping("/create_plan/{orderId}")
	public String createPlan(@PathVariable Long orderId, Model model) {
		model.addAttribute("orderId", orderId);
		return "createPlan";
	}

	@RequestMapping("/register")
	public String register(Model model) {
		return "register";
	}

	@RequestMapping("/success_url")
	public String success(Model model) {
		return "success";
	}

	@RequestMapping("/cancel_url")
	public String cancel(Model model) {
		return "cancel";
	}

	@RequestMapping("/error_url")
	public String error(Model model) {
		return "error";
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

}
