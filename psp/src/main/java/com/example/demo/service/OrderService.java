package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Order;
import com.example.demo.repo.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public Order save(Order order) {
		return orderRepository.save(order);
	}

	public Order findById(UUID id) throws NotFoundException {
		Optional<Order> order = orderRepository.findById(id);

		if (!order.isPresent()) {
			throw new NotFoundException(id.toString(), Order.class.getSimpleName());
		}

		return order.get();
	}

	public List<Order> findAll() {
		return orderRepository.findAll();
	}
}
