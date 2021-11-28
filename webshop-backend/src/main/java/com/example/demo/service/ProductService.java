package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductService {

	private final ProductRepository repo;

	@Transactional(readOnly = true)
	public List<Product> read() {
		return repo.findAll();
	}

	@Transactional
	public Product save(Product product) {
		return repo.save(product);
	}

	@Transactional
	public void delete(Long id) {
		repo.deleteById(id);
	}

}
