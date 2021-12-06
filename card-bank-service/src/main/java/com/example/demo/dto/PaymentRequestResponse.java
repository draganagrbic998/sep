package com.example.demo.dto;

import com.example.demo.model.PaymentRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PaymentRequestResponse {

	private String paymentId;
	private String paymentUrl;

	public PaymentRequestResponse(PaymentRequest request) {
		paymentId = request.getId().toString();
		paymentUrl = "http://localhost:8090/view/paymentForm";
	}

}