package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
	@NotBlank
	private String name;

	@Column
	@NotBlank
	private String description;

	@Column
	@NotNull
	@Positive
	private Double price;

	@Column
	@NotBlank
	private String category;

	@Column
	@NotBlank
	private String currency;

	@Column
	@NotBlank
	private String imageLocation;

	@OneToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private User user;

}
