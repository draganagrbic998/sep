package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesData {

	@Value("${paypal-products}")
	public String paypalProducts;

	@Value("${paypal-plans}")
	public String paypalPlans;

	@Value("${paypal-subscriptions}")
	public String paypalSubscriptions;

	@Value("${paypal-orders-checkout}")
	public String paypalOrdersCheckout;

	@Value("${success-url}")
	public String successUrl;

	@Value("${cancelUrl}")
	public String cancelUrl;

	@Value("${paypal-payment-url}")
	public String paypalPaymentUrl;

	@Value("${subscription-payment-url}")
	public String subscriptionPaymentUrl;

	@Value("${choose-type-url}")
	public String chooseTypeUrl;

}
