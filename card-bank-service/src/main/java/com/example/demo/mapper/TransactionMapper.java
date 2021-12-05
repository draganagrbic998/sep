package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.model.PaymentRequest;
import com.example.demo.model.Transaction;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Component
@Log4j2
public class TransactionMapper {

	private final DatabaseCipher cipher;

	public Transaction toEntity(PaymentRequest paymentRequest) {
		log.info("TransactionMapper - toEntity: paymentRequestId=" + paymentRequest.getId());
		Transaction model = new Transaction();

		model.setMerchantId(paymentRequest.getMerchantId());
		model.setAmount(paymentRequest.getAmount());
		model.setCurrency(paymentRequest.getCurrency());
		model.setMerchantOrderId(paymentRequest.getMerchantOrderId());
		model.setMerchantTimestamp(paymentRequest.getMerchantTimestamp());

		return cipher.encrypt(model);
	}

}
