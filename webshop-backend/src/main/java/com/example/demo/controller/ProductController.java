package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/product")
// add preauthorize
public class ProductController {

	private final ProductService service;

	@GetMapping
	public ResponseEntity<List<Product>> read() {
		return ResponseEntity.ok(service.read());
	}

	@PostMapping
	public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
		if (product.getId() != null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.save(product));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody Product product) {
		if (id == null || product.getId() == null || id != product.getId()) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.save(product));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delet(@PathVariable Long id) {
		if (id == null) {
			return ResponseEntity.badRequest().build();
		}
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
