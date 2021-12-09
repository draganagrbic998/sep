package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Customer {

	private String cardHolder;
	private String panNumber;
	private String cvv;
	private String yy;
	private String mm;

}
