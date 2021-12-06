package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction_table")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private Double amount;

	@NotBlank
	private String currency;

	@NotBlank
	private String merchantId;

	@NotNull
	private Long merchantOrderId;

	@NotNull
	private LocalDateTime merchantTimestamp;

	@NotNull
	private PaymentStatus status = PaymentStatus.SUCCESS;

	public Transaction(PaymentRequest request) {
		amount = request.getAmount();
		currency = request.getCurrency();
		merchantId = request.getMerchantId();
		merchantOrderId = request.getMerchantOrderId();
		merchantTimestamp = request.getMerchantTimestamp();
	}

}