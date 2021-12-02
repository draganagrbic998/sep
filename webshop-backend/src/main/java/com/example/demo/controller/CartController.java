package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderResponse;
import com.example.demo.model.CartItem;
import com.example.demo.service.CartService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {

	private final CartService service;

	@GetMapping
	public ResponseEntity<List<CartItem>> readCart() {
		log.info("CartController - readCart");
		return ResponseEntity.ok(service.readCart());
	}

	@PutMapping("/{productId}/add")
	public ResponseEntity<Void> addToCart(@PathVariable Long productId) {
		log.info("CartController - addToCart: productId=" + productId);

		if (productId == null) {
			log.error("addToCart - productId is null");
			return ResponseEntity.badRequest().build();
		}
		service.addToCart(productId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{productId}/remove")
	public ResponseEntity<Void> removeFromCart(@PathVariable Long productId) {
		log.info("CartController - removeFromCart: productId=" + productId);

		if (productId == null) {
			log.error("removeFromCart - productId is null");
			return ResponseEntity.badRequest().build();
		}
		service.removeFromCart(productId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/order")
	public ResponseEntity<Void> orderCart() {
		log.info("CartController - orderCart");
		service.orderCart();
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/orders")
	public ResponseEntity<List<OrderResponse>> readOrders() {
		log.info("CartController - readOrders");
		return ResponseEntity.ok(service.readOrders());
	}

}
