package com.example.demo.dto;

import com.example.demo.model.PaymentStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderStatusUpdate {
	private PaymentStatus status;
}
