package com.example.demo.dto;

import com.example.demo.model.OrderStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentCompletedDTO {

	private Integer orderId;
	private OrderStatus status;

}
