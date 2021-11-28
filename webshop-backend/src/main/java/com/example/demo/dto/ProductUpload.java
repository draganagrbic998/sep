package com.example.demo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductUpload {

	private Long id;

	@NotBlank(message = "Name cannot be blank")
	private String name;

	@NotBlank(message = "Description cannot be blank")
	private String description;

	@NotNull(message = "Price cannot be null")
	@Positive(message = "Price must be positive number")
	private Double price;

	@NotBlank(message = "Category cannot be blank")
	private String category;

	@NotBlank(message = "Currency cannot be blank")
	private String currency;

	@NotNull(message = "Image cannot be null")
	private MultipartFile image;

}
