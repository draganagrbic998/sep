package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {

	private Long id;
	private User user;
	private List<CartItem> items;

	public OrderDTO(Order order, List<CartItem> items) {
		id = order.getId();
		user = order.getUser();
		items.forEach(item -> {
			item.setCart(null);
			item.setOrder(null);
		});
		this.items = items;
	}

}
