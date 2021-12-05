package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String panNumber;

	@Column(nullable = false)
	private String cardHolder;

	@Column(nullable = false)
	private String cvv;

	@Column(nullable = false)
	private String expirationDate;

	@Column
	private String merchantId;

	@Column
	private String merchantPassword;

	@Column
	private Double availableFunds;

	@Column
	private Double reservedFunds;

}
