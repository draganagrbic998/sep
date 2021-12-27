package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesData {

	@Value("${bitcoin-orders}")
	public String bitcoinOrders;

	@Value("${success-url}")
	public String successUrl;

	@Value("${cancel-url}")
	public String cancelUrl;

	@Value("${bitcoin-payment-url}")
	public String bitcoinPaymentUrl;

}
