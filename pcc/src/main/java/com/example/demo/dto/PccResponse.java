package com.example.demo.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PccResponse {

	private Boolean authenticated;
	private Boolean transactionAuthorized;
	private Long acquirerOrderId;
	private Date acquirerTimestamp;
	private Long issuerOrderId;
	private Date issuerTimestamp;

}
