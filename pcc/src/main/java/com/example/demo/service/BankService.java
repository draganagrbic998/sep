package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Bank;
import com.example.demo.repo.BankRepository;

@Service
public class BankService {

	@Autowired
	private BankRepository repo;

	public Bank save(Bank bank) {
		return repo.save(bank);
	}

	public Bank getBankByPanNumber(String panNumberBankId) throws NotFoundException {
		Optional<Bank> bank = repo.findByPanNumberBankId(panNumberBankId);

		if (!bank.isPresent()) {
			throw new NotFoundException(panNumberBankId, Bank.class.getSimpleName());
		}

		return bank.get();
	}
}
