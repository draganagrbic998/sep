package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoingateOrderInfoDTO {

	private Integer id;
	private String order_id;
	private String status;
	private String created_at;
	private String expire_at;
	private String price_currency;
	private String price_amount;
	private String receive_currency;
	private String receive_amount;
	private String underpaid_amount;
	private String overpaid_amount;
	private String payment_address;
	private String payment_url;
	private Boolean is_refundable;

}
