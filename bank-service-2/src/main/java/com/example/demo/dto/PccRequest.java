package com.example.demo.dto;

import java.util.Date;

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
	private Long acquirerOrderId;
	private Date acquirerTimestamp;

	public PccRequest(Customer customer, PaymentRequest request, Long transactionId) {
		cardHolder = customer.getCardHolder();
		panNumber = customer.getPanNumber();
		cvv = customer.getCvv();
		yy = customer.getYy();
		mm = customer.getMm();
		amount = request.getAmount();
		currency = request.getCurrency();
		acquirerOrderId = transactionId;
		acquirerTimestamp = new Date();
	}

}
