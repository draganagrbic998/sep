package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.PaymentMethodDTO;
import com.example.demo.model.PaymentMethod;

@Component
public class PaymentMethodMapper {

	public PaymentMethod toEntity(PaymentMethodDTO dto) {
		PaymentMethod model = new PaymentMethod();

		model.setName(dto.getName());
		model.setActive(true);

		return model;
	}

}
