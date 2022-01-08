package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesData {

	@Value("${paypal-payment-url}")
	public String paypalPaymentUrl;

	@Value("${fetch-payment-url}")
	public String fetchPaymentUrl;

	@Value("${complete-payment-url}")
	public String completePaymentUrl;

	@Value("${subscription-payment-url}")
	public String subscriptionPaymentUrl;

	@Value("${fetch-subscription-url}")
	public String fetchSubscriptionUrl;

	@Value("${complete-subscription-url}")
	public String completeSubscriptionUrl;

	@Value("${paypal-products}")
	public String paypalProducts;

	@Value("${paypal-plans}")
	public String paypalPlans;

	@Value("${paypal-subscriptions}")
	public String paypalSubscriptions;

	@Value("${paypal-orders-checkout}")
	public String paypalOrdersCheckout;

	@Value("${register-url}")
	public String registerUrl;

	@Value("${choose-type-url}")
	public String chooseTypeUrl;

	@Value("${create-subscription-url}")
	public String createSubscriptionUrl;

	@Value("${success-url}")
	public String successUrl;

	@Value("${cancel-url}")
	public String cancelUrl;

}
