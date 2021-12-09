package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductUpload {

	private Long id;
	private String name;
	private String description;
	private String category;
	private Double price;
	private String currency;
	private MultipartFile image;

}
