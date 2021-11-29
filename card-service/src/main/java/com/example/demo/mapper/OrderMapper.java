package com.example.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dto.OrderDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;

@Component
public class OrderMapper {

	@Autowired
	OrderService service;

	public Order toEntity(OrderDTO dto) {
		Order model = new Order();

		model.setShopOrderId(dto.getOrderIdWebshop());
		model.setCurrency(dto.getCurrency());
		model.setPrice(dto.getPrice());
		model.setCallbackUrl(dto.getCallbackUrl());
		model.setOrderStatus(OrderStatus.CREATED);

		return model;
	}

}
