package com.example.demo.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.demo.dto.PaymentRequestDTO;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@AllArgsConstructor
public class PaymentRequestMapper {

	private final String url = "http://localhost:8087/view";
	private final DatabaseCipher databaseCipher;

	public PaymentRequestDTO toDTO(Merchant merchant, Order order) {
		log.info("PaymentRequestMapper - toDTO: merchantId=" + merchant.getMerchantId() + " orderId=" + order.getId());
		PaymentRequestDTO dto = new PaymentRequestDTO();

		merchant = databaseCipher.decrypt(merchant);

		dto.setMerchantId(merchant.getMerchantId());
		dto.setMerchantPassword(merchant.getMerchantPassword());
		dto.setAmount(order.getPrice());
		dto.setCurrency(order.getCurrency());
		dto.setMerchantOrderId(order.getId());
		dto.setMerchantTimestamp(LocalDateTime.now());

		dto.setSuccessUrl(url + "/success_url");
		dto.setFailedUrl(url + "/failed_url");
		dto.setErrorUrl(url + "/error_url");
		dto.setCallbackUrl("http://localhost:8762/card-service/card/complete");

		return dto;
	}

}
