package com.example.demo.dto;

import javax.validation.constraints.NotNull;

import com.example.demo.model.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class OrderStatusUpdate {

	@NotNull(message = "Order id cannot be null")
	private Long id;

	@NotNull(message = "Order status cannot be null")
	private OrderStatus status;

}
