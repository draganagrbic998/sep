package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.service.CartService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/cart")
// add preauthorize
public class CartController {

	private final CartService service;

	@GetMapping("/read_card")
	public ResponseEntity<Cart> readCart() {
		return ResponseEntity.ok(service.readCart());
	}

	@GetMapping("/read_orders")
	public ResponseEntity<List<Order>> readOrders() {
		return ResponseEntity.ok(service.readOrders());
	}

	@PutMapping("/add_to_cart/{productId}")
	public ResponseEntity<Cart> addToCart(@PathVariable Long productId) {
		if (productId == null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.addToCart(productId));
	}

	@PutMapping("/remove_from_cart/{productId}")
	public ResponseEntity<Cart> removeFromCart(@PathVariable Long productId) {
		if (productId == null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.removeFromCart(productId));
	}

	@PostMapping("/order_cart")
	public ResponseEntity<Cart> orderCart() {
		return ResponseEntity.ok(service.orderCart());
	}

}
