package com.example.demo.dto;

import com.example.demo.model.PaymentRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PccRequest {

	private String cardHolder;
	private String panNumber;
	private String cvv;
	private String yy;
	private String mm;
	private Double amount;
	private String currency;

	public PccRequest(PaymentRequest request, Customer client) {
		cardHolder = client.getCardHolder();
		panNumber = client.getPanNumber();
		cvv = client.getCvv();
		yy = client.getYy();
		mm = client.getMm();
		amount = request.getAmount();
		currency = request.getCurrency();
	}

}
