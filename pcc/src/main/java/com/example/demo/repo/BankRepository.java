package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

	Bank findByPanNumber(String panNumber);

}
