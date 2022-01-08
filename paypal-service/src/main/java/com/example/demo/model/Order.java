package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	private Long id;

	@Column
	private OrderStatus status = OrderStatus.CREATED;

	@Column
	private String payPalOrderId;

	@Column
	private String merchantApiKey;

	@Column
	private Double price;

	@Column
	private String currency;

	@Column
	private String callbackUrl;

}
