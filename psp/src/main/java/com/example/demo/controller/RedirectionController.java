package com.example.demo.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.OrderCreated;
import com.example.demo.model.Order;
import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/pay")
@Log4j2
public class RedirectionController {

	private final RestTemplate restTemplate;
	private final PropertiesData properties;

	@PostMapping("/{method}")
	public ResponseEntity<OrderCreated> pay(@PathVariable String method, @RequestBody Order dto) {
		log.info("RedirectionController - sending order to payment service: name=" + method);
		return ResponseEntity.ok(restTemplate.exchange(properties.zuulGatewayUrl + "/" + method, HttpMethod.POST,
				new HttpEntity<Order>(dto), OrderCreated.class).getBody());
	}
}
