package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.MerchantDTO;
import com.example.demo.model.Merchant;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class MerchantMapper {

	public Merchant toEntity(MerchantDTO dto) {
		log.info("MerchantMapper - toEntity");
		Merchant model = new Merchant();

		model.setClientId(dto.getClientId());
		model.setClientSecret(dto.getClientSecret());
		model.setMerchantApiKey(dto.getMerchantApiKey());

		return model;
	}

}
