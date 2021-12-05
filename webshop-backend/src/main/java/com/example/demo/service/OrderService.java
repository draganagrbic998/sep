package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class OrderService {

	private final OrderRepository repo;
	private final UserService userService;

	@Transactional(readOnly = true)
	public List<Order> read() {
		log.info("OrderService - read");
		return repo.findByUserId(userService.getLoggedInUser().getId());
	}

}
