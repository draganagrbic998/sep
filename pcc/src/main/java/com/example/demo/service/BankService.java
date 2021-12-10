package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.Bank;
import com.example.demo.repo.BankRepository;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class BankService {

	private final BankRepository repo;
	private final DatabaseCipher cipher;

	public Bank findByPanNumber(String panNumber) {
		log.info("BankService - findByPanNumber: panNumber=" + panNumber);
		return cipher.decrypt(repo.findByPanNumber(cipher.encrypt(panNumber)));
	}

}
