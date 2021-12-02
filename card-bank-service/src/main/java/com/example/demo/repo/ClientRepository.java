package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
	Optional<Client> findByMerchantId(String merchantId);

	Optional<Client> findByPanNumber(String panNumber);
}
