package com.example.demo.dto;

import com.example.demo.model.PaymentStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentCompletedDTO {

	private PaymentStatus status;

}
