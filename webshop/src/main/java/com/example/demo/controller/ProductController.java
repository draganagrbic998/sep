package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductUpload;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
@PreAuthorize("hasAuthority('merchant')")
public class ProductController {

	private final ProductService service;
	private final ProductMapper mapper;

	@GetMapping
	public ResponseEntity<Page<Product>> read(@RequestParam String category, Pageable pageable) {
		return ResponseEntity.ok(service.read(category, pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> readOne(@PathVariable Long id) {
		return ResponseEntity.ok(service.readOne(id));
	}

	@PostMapping
	public ResponseEntity<Product> create(@ModelAttribute ProductUpload dto) {
		try {
			return ResponseEntity.ok(service.save(mapper.map(dto), dto.getImage()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> update(@PathVariable Long id, @ModelAttribute ProductUpload dto) {
		try {
			return ResponseEntity.ok(service.save(mapper.map(dto), dto.getImage()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
