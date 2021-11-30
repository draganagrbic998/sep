package com.example.demo.mapper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dto.MerchantDTO;
import com.example.demo.model.Merchant;
import com.example.demo.service.MerchantService;

@Component
public class MerchantMapper {

	@Autowired
	MerchantService service;

	public Merchant toEntity(MerchantDTO dto, Integer merchantWebshopId) {
		Merchant model = new Merchant();

		Optional<Merchant> merchantOptional = service.findByMerchantWebshopIdOpional(merchantWebshopId);

		if (merchantOptional.isPresent())
			model.setId(merchantOptional.get().getId());

		model.setMerchantId(dto.getMerchantId());
		model.setMerchantPassword(dto.getMerchantPassword());
		model.setBankUrl(dto.getBankUrl());
		model.setMerchantWebshopId(merchantWebshopId);

		return model;
	}

}
