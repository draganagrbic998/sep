package com.example.demo.dto;

import java.time.LocalDateTime;

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
	private String mm;
	private String yy;
	private Long acquirerOrderId;
	private LocalDateTime acquirerTimestamp;
	private Double amount;
	private String currency;

	public PccRequest(PaymentRequest request, ClientDTO client, Long transactionId) {
		cardHolder = client.getCardHolder();
		panNumber = client.getPanNumber();
		cvv = client.getCvv();
		mm = client.getMm();
		yy = client.getYy();
		acquirerOrderId = transactionId;
		acquirerTimestamp = LocalDateTime.now();
		amount = request.getAmount();
		currency = request.getCurrency();

	}

}
