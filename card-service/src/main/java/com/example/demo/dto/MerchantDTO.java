package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MerchantDTO {

	private String merchantId;
	private String merchantPassword;
	private String bankUrl;

}
