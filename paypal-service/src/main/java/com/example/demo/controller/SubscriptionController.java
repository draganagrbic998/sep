package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SubscriptionDetailsDTO;
import com.example.demo.model.BillingPlan;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.Subscription;
import com.example.demo.service.BillingPlanService;
import com.example.demo.service.OrderService;
import com.example.demo.service.SubscriptionService;
import com.paypal.base.rest.PayPalRESTException;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/subscriptions")
@Log4j2
public class SubscriptionController {

	private final OrderService orderService;

	private final BillingPlanService billingPlanService;

	private final SubscriptionService subscriptionService;

	@PostMapping(value = "/{orderId}")
	public String create(@RequestBody SubscriptionDetailsDTO dto, @PathVariable Long orderId) {
		log.info("SubscriptionController - create");
		Order order = orderService.findById(orderId);

		Product product = billingPlanService.createProduct(order);
		BillingPlan plan = billingPlanService.createBillingPlan(order, product, dto.getDuration());

		Subscription subscription = subscriptionService.createSubscription(order, plan, dto);

		String redirectUrl = "https://localhost:8086/view/subscription_payment/" + subscription.getId();
		return redirectUrl;
	}

	@GetMapping("/{subscriptionId}")
	public ResponseEntity<String> getSubscriptionForPaypal(@PathVariable Long subscriptionId)
			throws PayPalRESTException {
		log.info("SubscriptionController - getSubscriptionForPaypal");
		return ResponseEntity.ok(subscriptionService.getSubscriptionDetails(subscriptionId));
	}

	@GetMapping(value = "/complete/{subscriptionId}/{subscriptionDBId}")
	public ResponseEntity<String> complete(@PathVariable String subscriptionId, @PathVariable Long subscriptionDBId) {
		log.info("SubscriptionController - activate");
		return ResponseEntity.ok(subscriptionService.completeSubscription(subscriptionId, subscriptionDBId));
	}
}