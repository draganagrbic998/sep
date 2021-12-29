package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesData {

	@Value("${view-url}")
	public String viewUrl;

	@Value("${complete-url}")
	public String completeUrl;

	@Value("${is-qr}")
	public Boolean isQr;

	@Value("${register-url}")
	public String registerUrl;

}
