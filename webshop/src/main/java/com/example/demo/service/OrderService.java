package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PspOrder;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.PaymentStatus;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class OrderService {

	private final OrderRepository repo;
	private final CartItemRepository cartRepo;
	private final UserService userService;
	private final RestTemplate restTemplate;
	private final PropertiesData properties;

	public List<Order> read() {
		log.info("OrderService - read");
		return repo.findByUserId(userService.getLoggedInUser().getId());
	}

	public Order updateStatus(Long id, PaymentStatus status) {
		log.info("OrderService - updateStatus: id=" + id);
		Order order = repo.findById(id).get();
		if (status.equals(PaymentStatus.SUCCESS)) {
			order.setStatus(OrderStatus.COMPLETED);
		} else {
			order.setStatus(OrderStatus.FAILED);
		}
		return repo.save(order);
	}

	public Order order(Long productId) {
		log.info("CartService - orderCart");
		CartItem item = cartRepo.findByUserIdAndProductId(userService.getLoggedInUser().getId(), productId);
		Order order = new Order(item);
		order = repo.save(order);
		cartRepo.deleteById(item.getId());

		PspOrder pspOrder = restTemplate.exchange(properties.pspUrl + "/orders", HttpMethod.POST,
				new HttpEntity<PspOrder>(new PspOrder(order, properties.callbackUrl + "/orders")), PspOrder.class)
				.getBody();
		order.setPspId(pspOrder.getId());
		return repo.save(order);
	}

}