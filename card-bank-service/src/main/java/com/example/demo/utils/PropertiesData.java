package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesData {

	@Value("${bankID}")
	public String bankId;

	@Value("${pccURL}")
	public String pccURL;

	@Value("${nbsAPI}")
	public String nbsAPI;

}
