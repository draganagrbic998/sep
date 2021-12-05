package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Merchant {

	@Id
	private Long id;

	@Column
	private String clientId;

	@Column
	private String clientSecret;

	@Column
	private String merchantApiKey;

}
