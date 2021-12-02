package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PCCRequestDTO;
import com.example.demo.dto.PCCResponseDTO;
import com.example.demo.mapper.PCCMapper;
import com.example.demo.model.Client;
import com.example.demo.utils.Utils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PCCService {

	@Autowired
	private PCCMapper pccMapper;

	@Autowired
	private ClientService clientService;

	public PCCResponseDTO pay(PCCRequestDTO pccRequestDTO) {
		log.info("PCCService - pay: acquirerOrderId=" + pccRequestDTO.getAcquirerOrderId());

		String clientBankId = pccRequestDTO.getPanNumber().replace("-", "").substring(1, 7);

		Optional<Client> clientOptional = clientService.findClientByPanNumber(pccRequestDTO.getPanNumber());

		if (!clientOptional.isPresent()) {
			log.error("Client: panNumber=" + clientBankId + "... not found.");
			return pccMapper.toFailedAuthPCCResponse(pccRequestDTO);
		}

		Client client = clientOptional.get();

		String tempDate = pccRequestDTO.getMm() + "/" + pccRequestDTO.getYy();

		if (!client.getCardHolder().equals(pccRequestDTO.getCardHolder())
				|| !client.getCvv().equals(pccRequestDTO.getCvv()) || !client.getExpirationDate().equals(tempDate)) {
			log.error("Client: panNumber=" + clientBankId + "... invalid card data entered");
			return pccMapper.toFailedAuthPCCResponse(pccRequestDTO);
		}

		// Proveravamo da li je istekla kartica
		if (Utils.cardExpired(client)) {
			log.error("Client: panNumber=" + clientBankId + "... card expired");
			return pccMapper.toFailedAuthPCCResponse(pccRequestDTO);
		}

		// Proveravamo da li ima dovoljno para na racunu
		if (pccRequestDTO.getAmount() > client.getAvailableFunds()) {
			log.error("Client: panNumber=" + clientBankId + "... not enough available funds");
			return pccMapper.toFailedPaymentPCCResponse(pccRequestDTO);
		}

		// Ovde bi trebalo API od NBS-a da se stavi da prebaci u dinari
		client.setAvailableFunds(client.getAvailableFunds() - pccRequestDTO.getAmount());
		clientService.save(client);

		return pccMapper.toSuccessfulPCCResponse(pccRequestDTO);
	}

}
