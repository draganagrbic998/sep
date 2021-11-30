package com.example.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dto.MerchantDTO;
import com.example.demo.model.Merchant;
import com.example.demo.service.MerchantService;

@Component
public class MerchantMapper {

	@Autowired
	MerchantService service;

	public Merchant toEntity(MerchantDTO dto) {
		Merchant model = new Merchant();

		model.setMerchantApiKey(dto.getMerchantApiKey());
		model.setMerchantId(dto.getMerchantApiKey());
		model.setMerchantPassword(dto.getMerchantPassword());
		model.setBankUrl(dto.getBankUrl());

		return model;
	}

}
