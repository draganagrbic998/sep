package com.example.demo.dto;

import com.example.demo.model.OrderStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderStatusUpdate {

	private Long id;
	private OrderStatus status;

}
