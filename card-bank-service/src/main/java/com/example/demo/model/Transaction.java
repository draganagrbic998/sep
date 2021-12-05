package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

	@Column
	private Double amount;

	@Column
	private String currency;

	@Column
	private String merchantId;

	@Column
	private Long merchantOrderId;

	@Column
	private LocalDateTime merchantTimestamp;

	@Column
	private String panNumber;

	@Column
	private TransactionStatus status;

}