package com.example.demo.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class RestConfig {

	private final RestTemplateBuilder restTemplateBuilder;

	@Bean
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = restTemplateBuilder.build();

		try {
			KeyStore keyStore = KeyStore.getInstance("JKS");
			InputStream in = new FileInputStream(ResourceUtils.getFile("classpath:keystore.jks"));
			keyStore.load(in, "password".toCharArray());
			in.close();

			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
					new SSLContextBuilder().loadTrustMaterial(keyStore, new TrustSelfSignedStrategy())
							.loadKeyMaterial(keyStore, "password".toCharArray()).build(),
					NoopHostnameVerifier.INSTANCE);

			HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).setMaxConnTotal(5)
					.setMaxConnPerRoute(5).build();

			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
					httpClient);

			requestFactory.setReadTimeout(10000);
			requestFactory.setConnectTimeout(10000);
			restTemplate.setRequestFactory(requestFactory);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return restTemplate;
	}

}
