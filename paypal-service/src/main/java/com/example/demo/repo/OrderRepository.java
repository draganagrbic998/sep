package com.example.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	Optional<Order> findByPayPalOrderId(String payPalOrderId);

	List<Order> findAllByExecuted(Boolean executed);

}
