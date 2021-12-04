package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.OrderCreatedDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@AllArgsConstructor
public class OrderMapper {

	private final DatabaseCipher databaseCipher;

	public Order toEntity(OrderDTO dto) {
		log.info("OrderMapper - toEntity");
		Order model = new Order();

		model.setShopOrderId(dto.getOrderIdWebShop());
		model.setMerchantApiKey(dto.getMerchantApiKey());
		model.setCurrency(dto.getCurrency());
		model.setPrice(dto.getPrice());
		model.setCallbackUrl(dto.getCallbackUrl());
		model.setOrderStatus(OrderStatus.CREATED);
		model.setTicks(0);

		return this.databaseCipher.encrypt(model);
	}

	public OrderCreatedDTO toDTO(Order model) {
		log.info("OrderMapper - toDTO: orderId=" + model.getId());
		OrderCreatedDTO dto = new OrderCreatedDTO();

		dto.setId(model.getId());
		dto.setStatus("CREATED");

		return dto;
	}

}
