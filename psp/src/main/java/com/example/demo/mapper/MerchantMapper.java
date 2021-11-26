package com.example.demo.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.dto.MerchantDTO;
import com.example.demo.model.Merchant;

@Component
public class MerchantMapper {

	public Merchant toEntity(MerchantDTO dto) {
		Merchant model = new Merchant();

		model.setName(dto.getName());
		model.setApiKey(UUID.randomUUID().toString());
		model.setActive(true);

		return model;
	}

}
