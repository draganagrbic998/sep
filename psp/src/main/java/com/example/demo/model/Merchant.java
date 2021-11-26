package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Where(clause = "active=true")
@Table(name = "merchant")
public class Merchant {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String apiKey;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<PaymentMethod> methods;

	@Column(nullable = false)
	private Boolean active;

	public Merchant() {
		this.methods = new HashSet<PaymentMethod>();
	}
}
