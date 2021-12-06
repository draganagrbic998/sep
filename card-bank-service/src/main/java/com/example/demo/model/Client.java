package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

	@NotBlank
	private String panNumber;

	@NotBlank
	private String cvv;

	@NotBlank
	private String expirationDate;

	@NotNull
	private Double availableFunds;

	@NotNull
	private Double reservedFunds;

	@NotBlank
	private String merchantId;

	@NotBlank
	private String merchantPassword;

	public void incAvailableFunds(Double amount) {
		availableFunds += amount;
	}

	public void decAvailableFunds(Double amount) {
		availableFunds -= amount;
	}

}
