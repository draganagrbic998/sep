package com.example.demo.dto;

import java.time.LocalDateTime;

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

	public PaymentRequest(Order order, String merchantId, String merchantPassword, String completeUrl, String viewUrl) {
		this.merchantId = merchantId;
		this.merchantPassword = merchantPassword;
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