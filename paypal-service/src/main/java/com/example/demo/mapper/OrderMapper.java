package com.example.demo.mapper;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dto.OrderDTO;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

public class OrderMapper {

	@Autowired
	OrderService service;

	public Order toEntity(OrderDTO dto) {
		Order model = new Order();

		model.setShopOrderId(dto.getShopOrderId());
		model.setMerchantId(dto.getMerchantId());
		model.setCurrency(dto.getCurrency());
		model.setValue(dto.getValue());
		model.setCallbackUrl(dto.getCallbackUrl());
		model.setRedirectUrl(dto.getRedirectUrl());
		model.setExecuted(false);
		model.setCreatedTimestamp(LocalDateTime.now());

		return model;
	}

}
