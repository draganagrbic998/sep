package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

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

	@NotBlank
	private String cardHolder;

	@Column(unique = true)
	@NotBlank
	private String panNumber;

	@NotBlank
	private String cvv;

	@NotBlank
	private String expirationDate;

	private String merchantId;
	private String merchantPassword;
	private Double availableFunds;
	private Double reservedFunds;

	public void incAvailableFunds(Double amount) {
		availableFunds += amount;
	}

	public void decAvailableFunds(Double amount) {
		availableFunds -= amount;
	}

}
