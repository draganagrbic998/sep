package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PCCRequestDTO;
import com.example.demo.dto.PCCResponseDTO;
import com.example.demo.mapper.PCCMapper;
import com.example.demo.model.Client;
import com.example.demo.utils.Utils;

@Service
public class PCCService {

	@Autowired
	private PCCMapper pccMapper;

	@Autowired
	private ClientService clientService;

	public PCCResponseDTO pay(PCCRequestDTO pccRequestDTO) {

		Optional<Client> clientOptional = clientService.findClientByPanNumber(pccRequestDTO.getPanNumber());

		if (!clientOptional.isPresent()) {
			return pccMapper.toFailedAuthPCCResponse(pccRequestDTO);
		}

		Client client = clientOptional.get();

		String tempDate = pccRequestDTO.getMm() + "/" + pccRequestDTO.getYy();

		if (!client.getCardHolder().equals(pccRequestDTO.getCardHolder())
				|| !client.getCvv().equals(pccRequestDTO.getCvv()) || !client.getExpirationDate().equals(tempDate)) {
			return pccMapper.toFailedAuthPCCResponse(pccRequestDTO);
		}

		// Proveravamo da li je istekla kartica
		if (Utils.cardExpired(client)) {
			return pccMapper.toFailedAuthPCCResponse(pccRequestDTO);
		}

		// Proveravamo da li ima dovoljno para na racunu
		if (pccRequestDTO.getAmount() > client.getAvailableFunds()) {
			return pccMapper.toFailedPaymentPCCResponse(pccRequestDTO);
		}

		// Ovde bi trebalo API od NBS-a da se stavi da prebaci u dinari
		client.setAvailableFunds(client.getAvailableFunds() - pccRequestDTO.getAmount());
		clientService.save(client);

		return pccMapper.toSuccessfulPCCResponse(pccRequestDTO);
	}

}
