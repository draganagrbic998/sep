package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Transaction;
import com.example.demo.repo.TransactionRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class TransactionService {

	@Autowired
	private TransactionRepository repo;

	public Transaction findByMerchantOrderId(Integer merchantOrderId) throws NotFoundException {
		log.info("TransactionService - findByMerchantOrderId: merchantOrderId=" + merchantOrderId.toString());
		Optional<Transaction> transaction = repo.findByMerchantOrderId(merchantOrderId);

		if (!transaction.isPresent()) {
			log.error("Transaction: merchantOrderId=" + merchantOrderId.toString() + " not found.");
			throw new NotFoundException(merchantOrderId.toString(), Transaction.class.getSimpleName(),
					"mechantOrderId");
		}

		return transaction.get();
	}

	public Transaction save(Transaction transaction) {
		log.info("TransactionService - save: id=" + transaction.getId().toString());
		return repo.save(transaction);
	}

}
