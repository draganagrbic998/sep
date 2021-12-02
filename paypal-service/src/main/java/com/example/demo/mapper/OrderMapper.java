package com.example.demo.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.demo.dto.OrderCreatedDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.model.Order;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class OrderMapper {

	public Order toEntity(OrderDTO dto) {
		log.info("OrderMapper - toEntity");
		Order model = new Order();

		model.setShopOrderId(dto.getOrderIdWebshop());
		model.setMerchantApiKey(dto.getMerchantApiKey());
		model.setCurrency(dto.getCurrency());
		model.setValue(dto.getPrice());
		model.setCallbackUrl(dto.getCallbackUrl());
		model.setExecuted(false);
		model.setCreatedTimestamp(LocalDateTime.now());

		return model;
	}

	public OrderCreatedDTO toDTO(Order model) {
		log.info("OrderMapper - toDTO: orderId=" + model.getId());
		OrderCreatedDTO dto = new OrderCreatedDTO();

		dto.setId(model.getId());
		dto.setStatus("CREATED");

		return dto;
	}

}
