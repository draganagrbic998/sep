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

	public PccResponse(PccRequest request, Long transactionId, boolean authenticated, boolean transactionAuthorized) {
		this.authenticated = authenticated;
		this.transactionAuthorized = transactionAuthorized;
		acquirerOrderId = request.getAcquirerOrderId();
		acquirerTimestamp = request.getAcquirerTimestamp();
		issuerOrderId = transactionId;
		issuerTimestamp = new Date();
	}

}
