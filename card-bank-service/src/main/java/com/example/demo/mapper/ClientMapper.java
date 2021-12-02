package com.example.demo.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ClientDTO;
import com.example.demo.model.Client;

@Component
public class ClientMapper {

	public Client toEntity(ClientDTO dto) {
		Client model = new Client();

		model.setMerchantId(UUID.randomUUID().toString());
		model.setPanNumber(dto.getPanNumber());
		model.setCardHolder(dto.getCardHolder());
		model.setCvv(dto.getCvv());
		model.setExpirationDate(dto.getMm() + "/" + dto.getYy());
		model.setAvailableFunds(Double.valueOf(1000000));
		model.setReservedFunds(Double.valueOf(0));
		model.setMerchantPassword(UUID.randomUUID().toString());

		return model;
	}

}
