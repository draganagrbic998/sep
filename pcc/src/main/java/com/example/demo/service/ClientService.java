package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Client;
import com.example.demo.repo.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repo;

	public Client save(Client client) {
		return repo.save(client);
	}

	public Optional<Client> findClientByPanNumber(String panNumber) {
		return repo.findByPanNumber(panNumber);
	}

	public Client getClientByMerchantId(String merchantId) throws NotFoundException {
		Optional<Client> client = repo.findByMerchantId(merchantId);

		if (!client.isPresent()) {
			throw new NotFoundException(merchantId, Client.class.getSimpleName());
		}

		return client.get();
	}

	public Client getClientByPanNumber(String panNumber) throws NotFoundException {
		Optional<Client> client = repo.findByPanNumber(panNumber);

		if (!client.isPresent()) {
			throw new NotFoundException(panNumber, Client.class.getSimpleName());
		}

		return client.get();
	}
}
