package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.model.PaymentRequest;
import com.example.demo.model.Transaction;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class TransactionMapper {

	public Transaction toEntity(PaymentRequest paymentRequest) {
		log.info("TransactionMapper - toEntity: paymentRequestId=" + paymentRequest.getId().toString());
		Transaction model = new Transaction();

		model.setMerchantId(paymentRequest.getMerchantId());
		model.setAmount(paymentRequest.getAmount());
		model.setCurrency(paymentRequest.getCurrency());
		model.setMerchantOrderId(paymentRequest.getMerchantOrderId());
		model.setMerchantTimestamp(paymentRequest.getMerchantTimestamp());

		return model;
	}

}
