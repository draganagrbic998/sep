package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/cart")
@PreAuthorize("hasAuthority('merchant')")
@Log4j2
public class CartController {

	private final CartService service;

	@GetMapping
	public ResponseEntity<List<CartItem>> read() {
		log.info("CartController - read");
		return ResponseEntity.ok(service.read());
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

	@PostMapping("/order/{productId}")
	public ResponseEntity<Void> orderCart(@PathVariable Long productId) {
		log.info("CartController - orderCart");

		if (productId == null) {
			log.error("orderCart - productId is null");
			return ResponseEntity.badRequest().build();
		}
		service.orderCart(productId);
		return ResponseEntity.noContent().build();
	}

}
