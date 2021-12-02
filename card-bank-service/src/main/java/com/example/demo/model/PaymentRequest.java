package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaymentRequest {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String merchantId;

	@Column(nullable = false)
	private String merchantPassword;

	@Column(nullable = false)
	private Double amount;

	@Column(nullable = false)
	private String currency;

	@Column(nullable = false)
	private Integer merchantOrderId;

	@Column(nullable = false)
	private LocalDateTime merchantTimestamp;

	@Column
	private String callbackUrl;

	@Column(nullable = false)
	private String successUrl;

	@Column(nullable = false)
	private String failedUrl;

	@Column(nullable = false)
	private String errorUrl;

}
