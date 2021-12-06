package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesData {

	@Value("${frontend-url}")
	public String frontendUrl;

	@Value("${zuul-gateway-url}")
	public String zuulGatewayUrl;

}
