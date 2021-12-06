package com.example.demo.dto;

import com.example.demo.model.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PaymentRequestCompleted {

	private Long id;
	private OrderStatus status;

}