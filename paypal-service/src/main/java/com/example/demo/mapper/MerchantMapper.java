package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.MerchantDTO;
import com.example.demo.model.Merchant;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Component
@Log4j2
public class MerchantMapper {

	private final DatabaseCipher databaseCipher;

	public Merchant toEntity(MerchantDTO dto) {
		log.info("MerchantMapper - toEntity");
		Merchant model = new Merchant();

		model.setClientId(dto.getClientId());
		model.setClientSecret(dto.getClientSecret());
		model.setMerchantApiKey(dto.getMerchantApiKey());

		return this.databaseCipher.encrypt(model);
	}

}
