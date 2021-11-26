package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderWebshopDTO {

	private Integer id;
	private Double price;
	private String currency;
	private String callbackUrl;

}
