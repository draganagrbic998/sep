package com.example.demo.dto;

import com.example.demo.model.Order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PspOrder {

	private Long id;
	private String merchantApiKey;
	private Double price;
	private String currency;
	private String callbackUrl;

	public PspOrder(Order order, String callbackUrl) {
		merchantApiKey = order.getUser().getApiKey();
		price = order.getQuantity() * order.getProduct().getPrice();
		currency = order.getProduct().getCurrency();
		this.callbackUrl = callbackUrl + "/" + order.getId();
	}

}
