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
	private String callbackUrl;
	private String successUrl;
	private String failUrl;
	private String errorUrl;
	private Long merchantOrderId;
	private LocalDateTime merchantTimestamp;

	public PaymentRequest(Merchant merchant, Order order, String completeUrl, String viewUrl) {
		merchantId = merchant.getMerchantId();
		merchantPassword = merchant.getMerchantPassword();
		amount = order.getPrice();
		currency = order.getCurrency();
		callbackUrl = completeUrl + "/" + order.getId();
		successUrl = viewUrl + "/success";
		failUrl = viewUrl + "/fail";
		errorUrl = viewUrl + "/error";
		merchantOrderId = order.getId();
		merchantTimestamp = LocalDateTime.now();
	}

}