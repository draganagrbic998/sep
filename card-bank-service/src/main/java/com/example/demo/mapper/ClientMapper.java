package com.example.demo.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ClientDTO;
import com.example.demo.model.Client;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ClientMapper {

	public Client map(ClientDTO dto) {
		Client model = new Client();

		model.setCardHolder(dto.getCardHolder());
		model.setPanNumber(dto.getPanNumber());
		model.setCvv(dto.getCvv());
		model.setExpirationDate(dto.getMm() + "/" + dto.getYy());

		model.setAvailableFunds(1000000.0);
		model.setReservedFunds(0.0);
		model.setMerchantId(UUID.randomUUID().toString());
		model.setMerchantPassword(UUID.randomUUID().toString());

		return model;
	}

}
