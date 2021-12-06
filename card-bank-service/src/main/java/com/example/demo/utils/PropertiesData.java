package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesData {

	@Value("${payment-url}")
	public String paymentUrl;

	@Value("${confirm-url}")
	public String confirmUrl;

	@Value("${bank-ID}")
	public String bankId;

	@Value("${pcc-url}")
	public String pccURL;

	@Value("${nbs-API}")
	public String nbsApi;

}
