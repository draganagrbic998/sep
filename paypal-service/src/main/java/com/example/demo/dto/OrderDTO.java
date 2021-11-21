package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

	private Double value;
	
	private String currency;

	private Integer merchantId;

	private Integer shopOrderId;

	private String redirectUrl;

	private String callbackUrl;

}
