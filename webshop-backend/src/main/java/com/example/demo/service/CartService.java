package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.OrderResponse;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CartService {

	private final CartItemRepository itemRepo;
	private final ProductRepository productRepo;
	private final OrderRepository orderRepo;
	private final UserService userService;

	@Transactional(readOnly = true)
	public List<CartItem> readCart() {
		return itemRepo.findByUserIdAndOrderIdIsNull(userService.getLoggedInUser().getId());
	}

	@Transactional
	public void addToCart(Long productId) {
		CartItem item = itemRepo.findByUserIdAndProductIdAndOrderIsNull(userService.getLoggedInUser().getId(),
				productId);

		if (item == null) {
			itemRepo.save(new CartItem(userService.getLoggedInUser(), productRepo.findById(productId).get()));
		} else {
			item.incQuantity();
			itemRepo.save(item);
		}
	}

	@Transactional
	public void removeFromCart(Long productId) {
		itemRepo.deleteById(itemRepo
				.findByUserIdAndProductIdAndOrderIsNull(userService.getLoggedInUser().getId(), productId).getId());
	}

	@Transactional
	public void orderCart() {
		Order order = new Order(userService.getLoggedInUser());
		orderRepo.save(order);

		List<CartItem> items = readCart();
		items.forEach(item -> item.setOrder(order));
		itemRepo.saveAll(items);
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> readOrders() {
		List<Order> orders = orderRepo.findByUserId(userService.getLoggedInUser().getId());
		return orders.stream().map(order -> new OrderResponse(order, itemRepo.findByOrderId(order.getId())))
				.collect(Collectors.toList());
	}

}
