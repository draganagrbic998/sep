package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
