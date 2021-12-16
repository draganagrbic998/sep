package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String subscriptionId;

	@Column
	private String planId;

	@Column
	private Long orderId;

	@Column
	private Double price;

	@Column
	private String currency;

	@Column
	private String subscriber;

	@Column
	private Long duration;

	@Column
	private String status;

	@Column
	private LocalDateTime createdTimestamp;

	@Column
	private String callbackUrl;

	@Column
	private String approveUrl;

	public Subscription() {
		this.createdTimestamp = LocalDateTime.now();
	}
}
