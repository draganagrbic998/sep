package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class CoingateOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String order_id;

	@Column
	private Double price_amount;

	@Column
	private String price_currency = "BTC";

	@Column
	private String receive_currency = "DO_NOT_CONVERT";

	@Column
	private String token;

	@Column
	private String title;

	@Column
	private String description;

	@Column
	private String success_url;

	@Column
	private String cancel_url;

	@Column
	private String payment_url;

	@Column
	private String callback_url;

}
