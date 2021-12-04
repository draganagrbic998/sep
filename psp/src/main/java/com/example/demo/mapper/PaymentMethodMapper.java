package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.PaymentMethodDTO;
import com.example.demo.model.PaymentMethod;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class PaymentMethodMapper {

	public PaymentMethod toEntity(PaymentMethodDTO dto) {
		log.info("PaymentMethodMapper - toEntity");
		PaymentMethod model = new PaymentMethod();

		model.setName(dto.getName());

		return model;
	}

}
