package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant, Integer> {

}
