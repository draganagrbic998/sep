package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {

	private Long id;
	private Double price;
	private String currency;
	private String callbackUrl;

}
