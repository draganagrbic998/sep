package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {

	private Long orderIdWebshop;
	private Double price;
	private String currency;
	private String merchantApiKey;
	private String callbackUrl;

}
