package com.example.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dto.OrderCreatedDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class OrderMapper {

	@Autowired
	OrderService service;

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

		return model;
	}

	public OrderCreatedDTO toDTO(Order model) {
		log.info("OrderMapper - toDTO: orderId=" + model.getId().toString());
		OrderCreatedDTO dto = new OrderCreatedDTO();

		dto.setId(model.getId());
		dto.setStatus("CREATED");

		return dto;
	}

}
