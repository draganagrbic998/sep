package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.MerchantDTO;
import com.example.demo.model.Merchant;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@AllArgsConstructor
public class MerchantMapper {

	private final DatabaseCipher databaseCipher;

	public Merchant toEntity(MerchantDTO dto) {
		log.info("MerchantMapper - toEntity");
		Merchant model = new Merchant();

		model.setMerchantApiKey(dto.getMerchantApiKey());
		model.setMerchantId(dto.getMerchantApiKey());
		model.setMerchantPassword(dto.getMerchantPassword());
		model.setBankUrl(dto.getBankUrl());

		return this.databaseCipher.encrypt(model);
	}

}
