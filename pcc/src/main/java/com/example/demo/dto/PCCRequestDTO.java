package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PCCRequestDTO {

	private String panNumber;
	private String cardHolder;
	private String cvv;
	private String mm;
	private String yy;
	private Long acquirerOrderId;
	private LocalDateTime acquirerTimestamp;
	private Double amount;
	private String currency;

}
