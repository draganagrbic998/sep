package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClientDTO {

	private String panNumber;
	private String cardHolder;
	private String cvv;
	private String mm;
	private String yy;

}
