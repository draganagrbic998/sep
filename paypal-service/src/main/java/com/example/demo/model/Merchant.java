package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Merchant {

	@EqualsAndHashCode.Include
	@Id
	private Integer id;

	@Column
	private String clientId;

	@Column
	private String clientSecret;

	@Column
	private String merchantApiKey;

}
