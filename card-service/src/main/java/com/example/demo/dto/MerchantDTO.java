package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MerchantDTO {

	private String merchantApiKey;
	private String merchantId;
	private String merchantPassword;
	private String bankUrl;

}
