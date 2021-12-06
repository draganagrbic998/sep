package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Client;
import com.example.demo.repo.ClientRepository;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class ClientService {

	private final ClientRepository repo;
	private final DatabaseCipher cipher;

	@Transactional
	public Client save(Client client) {
		log.info("ClientService - save: id=" + client.getId());
		return repo.save(cipher.encrypt(client));
	}

	public Optional<Client> findClientByPanNumber(String panNumber) {
		log.info("ClientService - findClientByPanNumber: panNumber=" + panNumber);
		return repo.findByPanNumber(panNumber);
	}

	public Client getClientByMerchantId(String merchantId) {
		log.info("ClientService - getClientByMerchantId: merchantId=" + merchantId);
		Optional<Client> client = repo.findByMerchantId(merchantId);

		if (!client.isPresent()) {
			log.error("Client: merchantId=" + merchantId + " not found.");
			throw new NotFoundException(merchantId, Client.class.getSimpleName());
		}

		return client.get();
	}

	public Client getClientByPanNumber(String panNumber) {
		log.info("ClientService - getClientByPanNumber: panNumber=" + panNumber.replace("-", "").substring(1, 7));
		Optional<Client> client = repo.findByPanNumber(panNumber);

		if (!client.isPresent()) {
			log.error("Client: panNumber=" + panNumber.replace("-", "").substring(1, 7) + " not found.");
			throw new NotFoundException(panNumber, Client.class.getSimpleName());
		}

		return client.get();
	}

}
