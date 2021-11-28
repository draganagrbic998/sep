package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PCCRequestDTO {

	private String panNumber;
	private String cardHolder;
	private Integer cvv;
	private String mm;
	private String yy;
	private Integer acquirerOrderId;
	private LocalDateTime acquirerTimestamp;
	private Double amount;
	private String currency;

}
