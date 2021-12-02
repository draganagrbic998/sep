package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ProductUpload;
import com.example.demo.model.Product;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ProductMapper {

	public Product map(ProductUpload dto) {
		log.info("ProductMapper - map");

		Product model = new Product();
		model.setId(dto.getId());
		model.setName(dto.getName());
		model.setDescription(dto.getDescription());
		model.setPrice(dto.getPrice());
		model.setCategory(dto.getCategory());
		model.setCurrency(dto.getCurrency());
		return model;
	}

}
