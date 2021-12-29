package com.example.demo.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PccRequest {

	private String cardHolder;
	private String panNumber;
	private String cvv;
	private String yy;
	private String mm;
	private Double amount;
	private String currency;
	private Long acquirerOrderId;
	private Date acquirerTimestamp;

}
