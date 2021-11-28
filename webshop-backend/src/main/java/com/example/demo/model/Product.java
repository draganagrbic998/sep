package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@NotBlank(message = "Name cannot be blank")
	private String name;

	@Column
	@NotBlank(message = "Description cannot be blank")
	private String description;

	@Column
	@NotNull(message = "Price cannot be null")
	@Positive(message = "Price must be positive number")
	private Double price;

	@Column
	@NotBlank(message = "Currency cannot be blank")
	private String currency;

}
