package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientDTO {

	private String panNumber;
	private String cardHolder;
	private Integer cvv;
	private String mm;
	private String yy;

}
