package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductService {

	private final ProductRepository repo;
	private final FileService fileService;

	@Transactional(readOnly = true)
	public List<Product> read() {
		return repo.findAll();
	}

	@Transactional
	public Product save(Product product, MultipartFile image) throws IOException {
		product = repo.save(product);
		product.setImageLocation(fileService.store(image));
		return product;
	}

	@Transactional
	public void delete(Long id) {
		repo.deleteById(id);
	}

}
