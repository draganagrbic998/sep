package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Bank;
import com.example.demo.repo.BankRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class BankService {

	private final BankRepository repo;

	@Transactional(readOnly = true)
	public Bank findByPanNumber(String panNumber) {
		log.info("BankService - findByPanNumber: panNumber=" + panNumber);
		return repo.findByPanNumber(panNumber);
	}

}
