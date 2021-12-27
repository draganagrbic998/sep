package com.example.demo.dto;

import com.example.demo.model.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentCompletedDTO {

	private PaymentStatus status;

}
