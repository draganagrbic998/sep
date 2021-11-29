package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repo.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repo;

	@Autowired
	private MerchantService merchantService;

}
