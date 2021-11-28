package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CartDTO {

	private Long id;
	private User user;
	private List<CartItem> items;

	public CartDTO(Cart cart, List<CartItem> items) {
		id = cart.getId();
		user = cart.getUser();
		items.forEach(item -> {
			item.setCart(null);
			item.setOrder(null);
		});
		this.items = items;
	}

}
