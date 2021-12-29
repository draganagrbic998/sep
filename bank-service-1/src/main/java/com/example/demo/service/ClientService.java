package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class ClientService {

	private final ClientRepository repo;
	private final DatabaseCipher cipher;

	public Client save(Client client) {
		log.info("ClientService - save: id=" + client.getId());
		return repo.save(cipher.encrypt(client));
	}

}
