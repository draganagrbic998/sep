package com.example.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	Optional<Order> findByPayPalOrderId(String payPalOrderId);

	List<Order> findAllByExecuted(Boolean executed);

}
