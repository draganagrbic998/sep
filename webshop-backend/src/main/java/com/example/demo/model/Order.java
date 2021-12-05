package com.example.demo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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

	@ManyToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private User user;

	@Column
	@NotNull
	private Date date;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@NotNull
	private Product product;

	@Column
	@NotNull
	@Positive
	private Long quantity;

	public Order(CartItem item) {
		date = new Date();
		user = item.getUser();
		product = item.getProduct();
		quantity = item.getQuantity();
	}

}
