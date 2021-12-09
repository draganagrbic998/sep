package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/cart")
@PreAuthorize("hasAuthority('merchant')")
public class CartController {

	private final CartService service;

	@GetMapping
	public ResponseEntity<List<CartItem>> read() {
		return ResponseEntity.ok(service.read());
	}

	@PutMapping("/{productId}/add")
	public ResponseEntity<Void> addToCart(@PathVariable Long productId) {
		service.addToCart(productId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{productId}/remove")
	public ResponseEntity<Void> removeFromCart(@PathVariable Long productId) {
		service.removeFromCart(productId);
		return ResponseEntity.noContent().build();
	}

}
