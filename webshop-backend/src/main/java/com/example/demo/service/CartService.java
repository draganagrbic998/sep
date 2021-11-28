package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.OrderDTO;
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
	public CartDTO readCart() {
		Cart cart = cartRepo.findByUserId(userService.getLoggedInUser().getId());
		return new CartDTO(cart, itemRepo.findByCartId(cart.getId()));
	}

	@Transactional(readOnly = true)
	public List<OrderDTO> readOrders() {
		List<Order> orders = orderRepo.findByUserId(userService.getLoggedInUser().getId());
		return orders.stream().map(order -> new OrderDTO(order, itemRepo.findByOrderId(order.getId())))
				.collect(Collectors.toList());
	}

	@Transactional
	public CartDTO addToCart(Long productId) {
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
	public CartDTO removeFromCart(Long productId) {
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
	public CartDTO orderCart() {
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
