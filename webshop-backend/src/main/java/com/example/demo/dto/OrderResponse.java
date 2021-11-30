package com.example.demo.dto;

import java.util.Date;
import java.util.List;

import com.example.demo.model.CartItem;
import com.example.demo.model.Order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderResponse {

	private Long id;
	private Date date;
	private List<CartItem> items;

	public OrderResponse(Order order, List<CartItem> items) {
		id = order.getId();
		date = order.getDate();
		items.forEach(item -> item.setOrder(null));
		this.items = items;
	}

}
