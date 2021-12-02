package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.model.PaymentRequest;
import com.example.demo.model.Transaction;

@Component
public class TransactionMapper {

	public Transaction toEntity(PaymentRequest paymentRequest) {
		Transaction model = new Transaction();

		model.setMerchantId(paymentRequest.getMerchantId());
		model.setAmount(paymentRequest.getAmount());
		model.setCurrency(paymentRequest.getCurrency());
		model.setMerchantOrderId(paymentRequest.getMerchantOrderId());
		model.setMerchantTimestamp(paymentRequest.getMerchantTimestamp());

		return model;
	}

}
