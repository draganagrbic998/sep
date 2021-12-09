package com.example.demo.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class ProductService {

	private final ProductRepository repo;
	private final UserService userService;
	private final FileService fileService;
	private final PropertiesData properties;

	public Page<Product> read(String category, Pageable pageable) {
		log.info("ProductService - read: category=" + category);
		if (category.equalsIgnoreCase("all")) {
			return repo.findAll(pageable);
		}
		if (category.equalsIgnoreCase("personal")) {
			return repo.findByUserId(userService.getLoggedInUser().getId(), pageable);
		}
		return repo.findByCategory(category, pageable);
	}

	public Product readOne(Long id) {
		log.info("ProductService - readOne: id=" + id);
		return repo.findById(id).get();
	}

	public Product save(Product product, MultipartFile image) throws IOException {
		log.info("ProductService - save: id=" + product.getId());
		product.setUser(userService.getLoggedInUser());
		if (image != null) {
			product.setImageLocation(properties.hostUrl + "/" + fileService.store(image));
		} else if (product.getId() != null) {
			product.setImageLocation(readOne(product.getId()).getImageLocation());
		}
		product = repo.save(product);
		return product;
	}

	public void delete(Long id) {
		log.info("ProductService - delete: id=" + id);
		repo.deleteById(id);
	}

}
