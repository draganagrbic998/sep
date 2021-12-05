package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AcquirerResponseDTO {

	private Boolean authentificated;
	private Boolean transactionAuthorized;

}
