package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.model.Merchant;
import com.example.demo.model.Order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PaymentRequest {

	private String merchantId;
	private String merchantPassword;
	private Double amount;
	private String currency;
	private Long merchantOrderId;
	private LocalDateTime merchantTimestamp;
	private String callbackUrl;
	private String successUrl;
	private String failUrl;
	private String errorUrl;

	public PaymentRequest(Merchant merchant, Order order) {
		merchantId = merchant.getMerchantId();
		merchantPassword = merchant.getMerchantPassword();
		amount = order.getPrice();
		currency = order.getCurrency();
		merchantOrderId = order.getId();
		merchantTimestamp = LocalDateTime.now();
		final String url = "http://localhost:8087/view";
		callbackUrl = "http://localhost:8087/complete/" + order.getId();
		successUrl = url + "/success";
		failUrl = url + "/fail";
		errorUrl = url + "/error";
	}

}