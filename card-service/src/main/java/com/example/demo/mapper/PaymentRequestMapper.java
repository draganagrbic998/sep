package com.example.demo.mapper;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dto.PaymentRequestDTO;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

@Component
public class PaymentRequestMapper {

	private final String url = "http://localhost:8087/view";

	@Autowired
	OrderService service;

	public PaymentRequestDTO toDTO(Merchant merchant, Order order) {
		PaymentRequestDTO dto = new PaymentRequestDTO();

		dto.setMerchantId(merchant.getMerchantId());
		dto.setMerchantPassword(merchant.getMerchantPassword());
		dto.setAmount(order.getPrice());
		dto.setCurrency(order.getCurrency());
		dto.setMerchantOrderId(order.getId());
		dto.setMerchantTimestamp(LocalDateTime.now());

		dto.setSuccessUrl(url + "/success_url");
		dto.setFailedUrl(url + "/failed_url");
		dto.setErrorUrl(url + "/error_url");
		dto.setCallbackUrl("http://localhost:8762/card-service");

		return dto;
	}

}
