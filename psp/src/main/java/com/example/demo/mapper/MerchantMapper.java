package com.example.demo.mapper;

import java.util.UUID;

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

		model.setName(dto.getName());
		model.setApiKey(UUID.randomUUID().toString());
		model.setActive(true);

		return model;
	}

}
