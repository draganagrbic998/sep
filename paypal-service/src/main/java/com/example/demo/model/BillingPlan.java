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
public class BillingPlan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String planId;

	@Column
	private String productId;

	@Column
	private String name;

	@Column
	private String status;

	@Column
	private LocalDateTime createdTimestamp;
	
	public BillingPlan() {
		this.createdTimestamp = LocalDateTime.now();
	}

}
