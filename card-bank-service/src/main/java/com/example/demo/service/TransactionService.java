package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Transaction;
import com.example.demo.repo.TransactionRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class TransactionService {

	private final TransactionRepository repo;

	@Transactional
	public Transaction save(Transaction transaction) {
		log.info("TransactionService - save: id=" + transaction.getId());
		return repo.save(transaction);
	}

}
