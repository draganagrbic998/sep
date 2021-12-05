package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PCCResponseDTO {

	private Long acquirerOrderId;
	private LocalDateTime acquirerTimestamp;
	private Boolean authentificated;
	private Boolean transactionAutorized;

}
