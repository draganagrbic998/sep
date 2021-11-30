package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderCreateDTO {

	private String merchantApiKey;
	private Integer orderIdWebShop;
	private Double price;
	private String currency;
	private String callbackUrl;

}