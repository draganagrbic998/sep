package com.example.demo.model;

import java.time.LocalDateTime;

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
	private String payPalOrderId;

	@Column
	private LocalDateTime createdTimestamp;

	@Column
	private Long shopOrderId;

	@Column
	private String merchantApiKey;

	@Column
	private OrderStatus status;

	@Column
	private String currency;

	@Column
	private Double value;

	@Column
	private Boolean executed;

	@Column
	private String redirectUrl;

	@Column
	private String callbackUrl;
}
