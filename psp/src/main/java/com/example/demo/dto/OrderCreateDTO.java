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

	private Integer orderIdWebshop;
	private Double price;
	private String currency;
	private Integer merchantId;
	private String callbackUrl;

}