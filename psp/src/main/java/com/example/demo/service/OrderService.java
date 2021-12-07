package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.Order;
import com.example.demo.repo.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class OrderService {

	private final OrderRepository repo;

	public Order save(Order order) {
		log.info("OrderService - save: id=" + order.getId());
		return repo.save(order);
	}

	public Order readOne(Long id) {
		log.info("OrderService - readOne: id=" + id);
		return repo.findById(id).get();
	}

}
