package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	public CartItem(User user, Product product) {
		this.user = user;
		this.product = product;
		this.quantity = 1l;
	}

	public void incQuantity() {
		++quantity;
	}

}
