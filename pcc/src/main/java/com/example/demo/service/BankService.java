package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Bank;
import com.example.demo.repo.BankRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class BankService {

	private final BankRepository repo;

	public Bank getBankByPanNumber(String panNumberBankId) throws NotFoundException {
		log.info("BankService - getBankByPanNumber: panNumberBankId=" + panNumberBankId);
		Optional<Bank> bank = repo.findByPanNumberBankId(panNumberBankId);

		if (!bank.isPresent()) {
			log.error("Bank: panNumberBankId=" + panNumberBankId + " not found.");
			throw new NotFoundException(panNumberBankId, Bank.class.getSimpleName());
		}

		return bank.get();
	}

	public Bank save(Bank bank) {
		bank = repo.save(bank);
		log.info("BankService - save: id=" + bank.getId());
		return bank;
	}

}
