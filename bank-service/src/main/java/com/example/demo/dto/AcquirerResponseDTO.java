package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcquirerResponseDTO {

	private Boolean authentificated;
	private Boolean transactionAutorized;

}
