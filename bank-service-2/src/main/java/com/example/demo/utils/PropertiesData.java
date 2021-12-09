package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesData {

	@Value("${bank-ID}")
	public String bankId;

	@Value("${nbs-API}")
	public String nbsApi;

	@Value("${pcc-url}")
	public String pccURL;

	@Value("${payment-url}")
	public String paymentUrl;

	@Value("${confirm-url}")
	public String confirmUrl;

}
