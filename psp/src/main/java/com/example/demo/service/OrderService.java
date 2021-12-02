package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.OrderStatusUpdateDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.repo.OrderRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class OrderService {

	@Autowired
	private OrderRepository repo;

	@Autowired
	private RestTemplate restTemplate;

	public List<Order> findAll() {
		log.info("OrderService - findAll");
		return repo.findAll();
	}

	public Order findById(Integer id) throws NotFoundException {
		log.info("OrderService - findById: id=" + id.toString());
		Optional<Order> order = repo.findById(id);

		if (!order.isPresent()) {
			log.error("Order: id=" + id.toString() + " not found.");
			throw new NotFoundException(id.toString(), Order.class.getSimpleName());
		}

		return order.get();
	}

	public Order save(Order order) {
		log.info("OrderService - save: id=" + order.getId().toString());
		return repo.save(order);
	}

	@Scheduled(fixedDelay = 60000) // svakih 60s proverimo stanje svih kreiranih porudzbina
	public void checkOrders() {
		log.info("OrderService - checkOrders");
		List<Order> orders = this.findAll();

		for (Order order : orders) {
			if (order.getStatus() == OrderStatus.CREATED) {
				// Ako je ispod 5 minuta sve je okej
				if (order.getTicks() < 5) {
					log.info(
							"Order: id=" + order.getId().toString() + " tick=" + order.getTicks().toString() + " - OK");
					order.setTicks(order.getTicks() + 1);
					this.save(order);
					// Javimo WebShop-u da je postupak slanja mikroservisu bio neuspesan
					// ako klijent nije odabrao nacin placanja u roku od 5 minuta
				} else {
					log.warn("Order: id=" + order.getId().toString() + " tick=" + order.getTicks().toString()
							+ " - FAILED");
					order.setStatus(OrderStatus.FAILED);
					this.save(order);

					OrderStatusUpdateDTO orderStatusUpdateDTO = new OrderStatusUpdateDTO();
					orderStatusUpdateDTO.setId(order.getOrderIdWebshop());
					orderStatusUpdateDTO.setStatus("FAILED");

					log.info("checkOrders - notifying WebShop @" + order.getCallbackUrl());
					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.POST,
							new HttpEntity<OrderStatusUpdateDTO>(orderStatusUpdateDTO), String.class);

				}
			}
		}
	}
}
