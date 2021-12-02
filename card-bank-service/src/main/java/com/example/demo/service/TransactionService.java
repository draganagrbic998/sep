package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Transaction;
import com.example.demo.repo.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository repo;

	public Transaction save(Transaction transaction) {
		return repo.save(transaction);
	}

	public Transaction findByMerchantOrderId(Integer merchantOrderId) throws NotFoundException {
		Optional<Transaction> transaction = repo.findByMerchantOrderId(merchantOrderId);

		if (!transaction.isPresent()) {
			throw new NotFoundException(merchantOrderId.toString(), Transaction.class.getSimpleName(),
					"mechantOrderId");
		}

		return transaction.get();
	}

}
