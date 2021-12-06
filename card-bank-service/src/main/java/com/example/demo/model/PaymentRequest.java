package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class PaymentRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String merchantId;

	@NotBlank
	private String merchantPassword;

	@NotNull
	private Long merchantOrderId;

	@NotNull
	private LocalDateTime merchantTimestamp;

	@NotNull
	@Positive
	private Double amount;

	@NotBlank
	private String currency;

	@NotBlank
	private String callbackUrl;

	@NotBlank
	private String successUrl;

	@NotBlank
	private String failUrl;

	@NotBlank
	private String errorUrl;

}
