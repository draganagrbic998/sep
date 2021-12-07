package com.example.demo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
	private Long id;

	@NotNull
	private OrderStatus status = OrderStatus.CREATED;

	@NotNull
	private Date date = new Date();

	@ManyToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private User user;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@NotNull
	private Product product;

	@NotNull
	private Long quantity;

	private Long pspId;

	public Order(CartItem item) {
		user = item.getUser();
		product = item.getProduct();
		quantity = item.getQuantity();
	}

}
