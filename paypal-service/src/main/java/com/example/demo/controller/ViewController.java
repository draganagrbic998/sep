package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.utils.DatabaseCipher;
import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final MerchantRepository merchantRepo;
	private final OrderRepository orderRepo;
	private final SubscriptionRepository subscriptionRepo;
	private final DatabaseCipher cipher;
	private final PropertiesData properties;

	@RequestMapping("/paypal_payment/{orderId}")
	public String paypalPayment(@PathVariable Long orderId, Model model) {
		model.addAttribute("orderId", orderId);
		model.addAttribute("fetchUrl", properties.fetchPaymentUrl);
		model.addAttribute("completeUrl", properties.completePaymentUrl);
		return "confirmOrder";
	}

	@RequestMapping("/subscription_payment/{subscriptionId}")
	public String subscriptionPayment(@PathVariable Long subscriptionId, Model model) {
		model.addAttribute("subscriptionId", subscriptionId);
		model.addAttribute("clientId",
				cipher.decrypt(merchantRepo.findByMerchantApiKey(
						orderRepo.findById(subscriptionRepo.findById(subscriptionId).get().getOrderId()).get()
								.getMerchantApiKey())
						.getClientId()));
		model.addAttribute("fetchUrl", properties.fetchSubscriptionUrl);
		model.addAttribute("completeUrl", properties.completeSubscriptionUrl);
		return "confirmSubscription";
	}

	@RequestMapping("/create_plan/{orderId}")
	public String createPlan(@PathVariable Long orderId, Model model) {
		model.addAttribute("orderId", orderId);
		model.addAttribute("createSubscriptionUrl", properties.createSubscriptionUrl);
		return "createPlan";
	}

	@RequestMapping("/choose_type/{orderId}")
	public String chooseType(@PathVariable Long orderId, Model model) {
		model.addAttribute("orderId", orderId);
		return "chooseType";
	}

	@RequestMapping("/register")
	public String register(Model model) {
		model.addAttribute("registerUrl", properties.registerUrl);
		return "register";
	}

	@RequestMapping("/success_url")
	public String success() {
		return "success";
	}

	@RequestMapping("/cancel_url")
	public String cancel() {
		return "cancel";
	}

	@RequestMapping("/error_url")
	public String error() {
		return "error";
	}

	@RequestMapping("/subscription_success_url")
	public String subscriptionSuccess() {
		return "subscriptionSuccess";
	}

	@RequestMapping("/subscription_cancel_url")
	public String subscriptionCancel() {
		return "subscriptionCancel";
	}

	@RequestMapping("/subscription_error_url")
	public String subscriptionError() {
		return "subscriptionError";
	}

}
