package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

	private String merchantApiKey;
	private Long orderIdWebShop;
	private Double price;
	private String currency;
	private String callbackUrl;

}
