package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.MerchantDTO;
import com.example.demo.model.Merchant;

@Component
public class MerchantMapper {

	public Merchant toEntity(MerchantDTO dto) {
		Merchant model = new Merchant();

		model.setClientId(dto.getClientId());
		model.setClientSecret(dto.getClientSecret());
		model.setMerchantApiKey(dto.getMerchantApiKey());

		return model;
	}

}
