package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

@Service
public class OrderService {

	@Autowired
	private OrderRepository repo;

	@Autowired
	private RestTemplate restTemplate;

	public Order save(Order order) {
		return repo.save(order);
	}

	public Order findById(UUID id) throws NotFoundException {
		Optional<Order> order = repo.findById(id);

		if (!order.isPresent()) {
			throw new NotFoundException(id.toString(), Order.class.getSimpleName());
		}

		return order.get();
	}

	public List<Order> findAll() {
		return repo.findAll();
	}

	@Scheduled(fixedDelay = 60000) // svakih 60s proverimo stanje svih kreiranih porudzbina
	public void checkOrders() {
		List<Order> orders = this.findAll();

		for (Order order : orders) {
			if (order.getStatus() == OrderStatus.CREATED) {
				// Ako je ispod 5 minuta sve je okej
				if (order.getTicks() < 5) {
					order.setTicks(order.getTicks() + 1);
					this.save(order);
					// Javimo WebShop-u da je postupak slanja mikroservisu bio neuspesan
					// ako klijent nije odabrao nacin placanja u roku od 5 minuta
				} else {
					order.setStatus(OrderStatus.FAILED);
					this.save(order);

					OrderStatusUpdateDTO orderStatusUpdateDTO = new OrderStatusUpdateDTO();
					orderStatusUpdateDTO.setId(order.getOrderIdWebshop());
					orderStatusUpdateDTO.setStatus("FAILED");

					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.POST,
							new HttpEntity<OrderStatusUpdateDTO>(orderStatusUpdateDTO), String.class);

				}
			}
		}
	}
}
