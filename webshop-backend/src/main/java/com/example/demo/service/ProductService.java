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
		if (category.equalsIgnoreCase("all")) {
			return repo.findAll(pageable);
		}
		if (category.equalsIgnoreCase("personal")) {
			return repo.findByUserId(userService.getLoggedInUser().getId(), pageable);
		}
		return repo.findByCategory(category, pageable);
	}

	@Transactional(readOnly = true)
	public Product readOne(Long id) {
		return repo.findById(id).get();
	}

	@Transactional
	public Product save(Product product, MultipartFile image) throws IOException {
		product.setUser(userService.getLoggedInUser());
		if (image != null) {
			product.setImageLocation("http://localhost:8080/" + fileService.store(image));
		} else if (product.getId() != null) {
			product.setImageLocation(readOne(product.getId()).getImageLocation());
		}
		return repo.save(product);
	}

	@Transactional
	public void delete(Long id) {
		repo.deleteById(id);
	}

}
