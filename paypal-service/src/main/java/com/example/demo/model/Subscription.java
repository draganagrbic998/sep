package com.example.demo.model;

import java.time.LocalDateTime;

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
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private LocalDateTime createdTimestamp = LocalDateTime.now();

	@Column
	private String status;

	@Column
	private Long orderId;

	@Column
	private String planId;

	@Column
	private String subscriptionId;

	@Column
	private String subscriber;

	@Column
	private Long duration;

	@Column
	private Double price;

	@Column
	private String currency;

	@Column
	private String callbackUrl;

	@Column
	private String approveUrl;

}
