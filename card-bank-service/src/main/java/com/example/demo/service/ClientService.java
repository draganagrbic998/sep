package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Client;
import com.example.demo.repo.ClientRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ClientService {

	@Autowired
	private ClientRepository repo;

	public Optional<Client> findClientByPanNumber(String panNumber) {
		log.info("ClientService - findClientByPanNumber: panNumber=" + panNumber);
		return repo.findByPanNumber(panNumber);
	}

	public Client getClientByMerchantId(String merchantId) throws NotFoundException {
		log.info("ClientService - getClientByMerchantId: merchantId=" + merchantId);
		Optional<Client> client = repo.findByMerchantId(merchantId);

		if (!client.isPresent()) {
			log.error("Client: merchantId=" + merchantId + " not found.");
			throw new NotFoundException(merchantId, Client.class.getSimpleName());
		}

		return client.get();
	}

	public Client getClientByPanNumber(String panNumber) throws NotFoundException {
		log.info("ClientService - getClientByPanNumber: panNumber=" + panNumber.replace("-", "").substring(1, 7));
		Optional<Client> client = repo.findByPanNumber(panNumber);

		if (!client.isPresent()) {
			log.error("Client: panNumber=" + panNumber.replace("-", "").substring(1, 7) + " not found.");
			throw new NotFoundException(panNumber, Client.class.getSimpleName());
		}

		return client.get();
	}

	public Client save(Client client) {
		log.info("ClientService - save: id=" + client.getId().toString());
		return repo.save(client);
	}
}
