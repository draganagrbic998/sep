package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_table")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@NotNull
	@Column
	private Integer orderIdWebshop;

	@NotNull
	@Column
	private Double price;

	@NotBlank
	@Column
	private String currency;

	@NotNull
	@Column
	private OrderStatus status;

	@Column
	private String callbackUrl;

	@Column
	private Integer ticks;

}
