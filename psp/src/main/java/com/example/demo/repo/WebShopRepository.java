package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.WebShop;

@Repository
public interface WebShopRepository extends JpaRepository<WebShop, Long> {

}
