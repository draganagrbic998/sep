package com.example.demo.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.OrderStatusUpdate;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.PaymentStatus;
import com.example.demo.repository.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class OrderService {

	private final OrderRepository repo;
	private final RestTemplate restTemplate;

	public Order save(Order order) {
		log.info("OrderService - save: id=" + order.getId());
		return repo.save(order);
	}

	public Order readOne(Long id) {
		log.info("OrderService - readOne: id=" + id);
		return repo.findById(id).get();
	}

	@Scheduled(fixedDelay = 60000)
	public void checkOrders() {
		log.info("OrderService - checkOrders");

		for (Order order : repo.findAll()) {
			if (order.getStatus().equals(OrderStatus.CREATED)) {
				if (order.getTicks() < 5) {
					log.info("Order: id=" + order.getId() + " tick=" + order.getTicks() + " - OK");
					order.setTicks(order.getTicks() + 1);
					repo.save(order);

				} else {
					log.warn("Order: id=" + order.getId() + " tick=" + order.getTicks() + " - FAILED");
					order.setStatus(OrderStatus.FAILED);
					repo.save(order);

					log.info("OrderService - checkOrders: notifying WebShop @" + order.getCallbackUrl());
					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.FAIL)), Void.class);

				}
			}
		}
	}

}
