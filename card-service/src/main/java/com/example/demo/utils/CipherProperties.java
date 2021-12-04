package com.example.demo.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "cipher")
public class CipherProperties {
	private String ipsPath;
	private String dbKeystorePath;
	private String dbKeystorePassword;
}
