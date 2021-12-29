package com.example.demo.model;

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
@Table(name = "transaction_table")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private OrderStatus status;

	@NotNull
	private Double amount;

	@NotBlank
	private String currency;

	@NotBlank
	private String merchantApiKey;

	@NotNull
	private Long orderId;

	public Transaction(Order order) {
		status = order.getStatus();
		amount = order.getQuantity() * order.getProduct().getPrice();
		currency = order.getProduct().getCurrency();
		merchantApiKey = order.getUser().getApiKey();
		orderId = order.getId();
	}

}
