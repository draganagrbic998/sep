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
	private PaymentStatus status = PaymentStatus.SUCCESS;

	@NotBlank
	private String merchantId;

	@NotNull
	private Double amount;

	@NotBlank
	private String currency;

	@NotNull
	private Long merchantOrderId;

	@NotNull
	private LocalDateTime merchantTimestamp;

	public Transaction(PaymentRequest request) {
		merchantId = request.getMerchantId();
		amount = request.getAmount();
		currency = request.getCurrency();
		merchantOrderId = request.getMerchantOrderId();
		merchantTimestamp = request.getMerchantTimestamp();
	}

}