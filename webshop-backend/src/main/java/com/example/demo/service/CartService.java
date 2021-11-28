package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CartService {

	private final CartRepository cartRepo;
	private final CartItemRepository itemRepo;
	private final ProductRepository productRepo;
	private final OrderRepository orderRepo;
	private final UserService userService;

	@Transactional(readOnly = true)
	public Cart readCart() {
		Cart cart = cartRepo.findByUserId(userService.getLoggedInUser().getId());
		List<CartItem> items = itemRepo.findByCartId(cart.getId());
		items.forEach(item -> {
			item.setCart(null);
			item.setOrder(null);
		});
		cart.setItems(items);
		return cart;
	}

	@Transactional(readOnly = true)
	public List<Order> readOrders() {
		List<Order> orders = orderRepo.findByUserId(userService.getLoggedInUser().getId());
		orders.forEach(order -> {
			List<CartItem> items = itemRepo.findByOrderId(order.getId());
			items.forEach(item -> {
				item.setCart(null);
				item.setOrder(null);
			});
			order.setItems(items);
		});
		return orders;
	}

	@Transactional
	public Cart addToCart(Long productId) {
		Cart cart = cartRepo.findByUserId(userService.getLoggedInUser().getId());
		Product product = productRepo.findById(productId).get();
		CartItem item = itemRepo.findByCartIdAndProductId(cart.getId(), product.getId());

		if (item == null) {
			itemRepo.save(new CartItem(cart, product));
		} else {
			item.incQuantity();
			itemRepo.save(item);
		}

		return readCart();
	}

	@Transactional
	public Cart removeFromCart(Long productId) {
		Cart cart = cartRepo.findByUserId(userService.getLoggedInUser().getId());
		Product product = productRepo.findById(productId).get();
		CartItem item = itemRepo.findByCartIdAndProductId(cart.getId(), product.getId());

		if (item.getQuantity() <= 1) {
			itemRepo.deleteById(item.getId());
		} else {
			item.decQuantity();
			itemRepo.save(item);
		}

		return readCart();
	}

	@Transactional
	public Cart orderCart() {
		Cart cart = cartRepo.findByUserId(userService.getLoggedInUser().getId());
		Order order = new Order();
		order.setUser(userService.getLoggedInUser());
		orderRepo.save(order);

		List<CartItem> items = itemRepo.findByCartId(cart.getId());
		items.forEach(item -> {
			item.setCart(null);
			item.setOrder(order);
		});

		itemRepo.saveAll(items);
		return readCart();
	}

}
