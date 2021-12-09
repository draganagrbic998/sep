package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PccResponse {

	private Boolean authenticated;
	private Boolean transactionAuthorized;

}
