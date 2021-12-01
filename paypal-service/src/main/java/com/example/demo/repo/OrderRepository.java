package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	Order findByPayPalOrderIdNotNull(String payPalOrderId);

	List<Order> findAllByExecuted(Boolean executed);

}
