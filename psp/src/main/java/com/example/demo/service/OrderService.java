package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.OrderStatusUpdate;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.repo.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class OrderService {

	private final OrderRepository repo;
	private final RestTemplate restTemplate;

	@Transactional
	public Order save(Order order) {
		order = repo.save(order);
		log.info("OrderService - save: id=" + order.getId());
		return order;
	}

	@Transactional(readOnly = true)
	public Order readOne(Long id) {
		log.info("OrderService - readOne: id=" + id);
		Optional<Order> order = repo.findById(id);

		if (!order.isPresent()) {
			log.error("Order: id=" + id + " not found.");
			throw new NotFoundException(id.toString(), Order.class.getSimpleName());
		}

		return order.get();
	}

	@Scheduled(fixedDelay = 60000)
	public void checkOrders() {
		log.info("OrderService - checkOrders");
		List<Order> orders = repo.findAll();

		for (Order order : orders) {
			if (order.getStatus().equals(OrderStatus.CREATED)) {
				if (order.getTicks() < 1) {
					log.info("Order: id=" + order.getId() + " tick=" + order.getTicks() + " - OK");
					order.setTicks(order.getTicks() + 1);
					save(order);
				} else {
					log.warn("Order: id=" + order.getId() + " tick=" + order.getTicks() + " - FAILED");
					order.setStatus(OrderStatus.FAILED);
					save(order);

					OrderStatusUpdate dto = new OrderStatusUpdate(order.getWebshopId(), OrderStatus.FAILED);

					log.info("checkOrders - notifying WebShop @" + order.getCallbackUrl());
					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<OrderStatusUpdate>(dto), String.class);

				}
			}
		}
	}
}
