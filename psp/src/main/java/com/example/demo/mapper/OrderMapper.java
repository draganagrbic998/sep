package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.OrderWebshopDTO;
import com.example.demo.model.Order;

@Component
public class OrderMapper {

	public Order toEntity(OrderWebshopDTO dto) {
		Order model = new Order();

		model.setOrderIdWebshop(dto.getId());
		model.setPrice(dto.getPrice());
		model.setCurrency(dto.getCurrency());
		model.setCallbackUrl(dto.getCallbackUrl());

		return model;
	}

}
