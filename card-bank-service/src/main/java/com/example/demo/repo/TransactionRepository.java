package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	Optional<Transaction> findByMerchantOrderId(Integer orderId);

}