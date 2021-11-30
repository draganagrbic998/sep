package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant, Integer> {

	Optional<Merchant> findByMerchantApiKey(String merchantApiKey);

}