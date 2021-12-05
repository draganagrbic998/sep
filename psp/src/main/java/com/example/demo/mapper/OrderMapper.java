package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.OrderDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class OrderMapper {

	public Order toEntity(OrderDTO dto) {
		log.info("OrderMapper - toEntity");
		Order model = new Order();

		model.setStatus(OrderStatus.CREATED);
		model.setWebshopId(dto.getId());
		model.setPrice(dto.getPrice());
		model.setCurrency(dto.getCurrency());
		model.setCallbackUrl(dto.getCallbackUrl());
		model.setTicks(0);

		return model;
	}

}
