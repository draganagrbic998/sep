package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesData {

	@Value("${register-url}")
	public String registerUrl;

	@Value("${bitcoin-orders-url}")
	public String bitcoinOrdersUrl;

	@Value("${bitcoin-payment-url}")
	public String bitcoinPaymentUrl;

	@Value("${success-url}")
	public String successUrl;

	@Value("${cancel-url}")
	public String cancelUrl;

	@Value("${complete-url}")
	public String completeUrl;

}
