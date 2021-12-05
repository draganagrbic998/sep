package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {

	private String merchantApiKey;
	private Long orderIdWebShop;
	private Double price;
	private String currency;
	private String callbackUrl;

}
