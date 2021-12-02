package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.PaymentRequestDTO;
import com.example.demo.dto.PaymentRequestResponseDTO;
import com.example.demo.model.PaymentRequest;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class PaymentRequestMapper {

	public PaymentRequest toEntity(PaymentRequestDTO dto) {
		log.info("PCCMapper - toEntity");
		PaymentRequest model = new PaymentRequest();

		model.setMerchantId(dto.getMerchantId());
		model.setMerchantPassword(dto.getMerchantPassword());
		model.setAmount(dto.getAmount());
		model.setCurrency(dto.getCurrency());
		model.setMerchantOrderId(dto.getMerchantOrderId());
		model.setMerchantTimestamp(dto.getMerchantTimestamp());
		model.setCallbackUrl(dto.getCallbackUrl());
		model.setSuccessUrl(dto.getSuccessUrl());
		model.setFailedUrl(dto.getFailedUrl());
		model.setErrorUrl(dto.getErrorUrl());

		return model;
	}

	public PaymentRequestResponseDTO toDTO(PaymentRequest model) {
		log.info("PCCMapper - toDTO: paymentRequestId=" + model.getId().toString());
		PaymentRequestResponseDTO dto = new PaymentRequestResponseDTO();

		dto.setPaymentId(model.getId().toString());
		dto.setPaymentUrl("http://localhost:8090/view/form");

		return dto;
	}

}
