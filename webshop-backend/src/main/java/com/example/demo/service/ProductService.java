package com.example.demo.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	private final UserService userService;
	private final FileService fileService;

	@Transactional(readOnly = true)
	public Page<Product> read(String category, Pageable pageable) {
		if (category == null) {
			return repo.findAll(pageable);
		}
		if (category.equalsIgnoreCase("personal")) {
			return repo.findByUserId(userService.getLoggedInUser().getId(), pageable);
		}
		return repo.findByCategory(category, pageable);
	}

	@Transactional
	public Product save(Product product, MultipartFile image) throws IOException {
		product.setUser(userService.getLoggedInUser());
		product.setImageLocation("http://localhost:8080/" + fileService.store(image));
		return repo.save(product);
	}

	@Transactional
	public void delete(Long id) {
		repo.deleteById(id);
	}

}
