package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PCCResponseDTO {

	private Integer acquirerOrderId;
	private LocalDateTime acquirerTimestamp;
	private Boolean authentificated;
	private Boolean transactionAutorized;

}
