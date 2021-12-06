package com.example.demo.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String merchantApiKey;

	@NotNull
	private Double price;

	@NotBlank
	private String currency;

	@NotBlank
	private String callbackUrl;

	@NotNull
	private OrderStatus status = OrderStatus.CREATED;

	@NotNull
	private Integer ticks = 0;

}