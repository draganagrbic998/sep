package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequestDTO {

	private String merchantId;
	private String merchantPassword;
	private Double amount;
	private String currency;
	private Long merchantOrderId;
	private LocalDateTime merchantTimestamp;
	private String callbackUrl;
	private String successUrl;
	private String failedUrl;
	private String errorUrl;

}