package com.example.demo.dto;

import com.example.demo.model.PaymentRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PaymentRequestResponse {

	private Long id;
	private String url;

	public PaymentRequestResponse(PaymentRequest request, String url) {
		id = request.getId();
		this.url = url;
	}

}