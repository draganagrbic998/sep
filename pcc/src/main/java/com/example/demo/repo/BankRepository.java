package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Bank;

public interface BankRepository extends JpaRepository<Bank, Integer> {

	Optional<Bank> findByPanNumberBankId(String panNumberBankId);

}
