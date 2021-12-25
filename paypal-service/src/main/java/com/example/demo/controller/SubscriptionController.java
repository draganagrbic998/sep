package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SubscriptionDTO;
import com.example.demo.service.SubscriptionService;
import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/subscriptions")
@Log4j2
public class SubscriptionController {

	private final SubscriptionService service;
	private final PropertiesData properties;

	@PostMapping(value = "/{orderId}")
	public ResponseEntity<String> create(@PathVariable Long orderId, @RequestBody SubscriptionDTO dto) {
		log.info("SubscriptionController - create");
		return ResponseEntity.ok(properties.subscriptionPaymentUrl + "/" + service.create(orderId, dto).getId());
	}

	@GetMapping("/{subscriptionId}")
	public ResponseEntity<String> getDetails(@PathVariable Long subscriptionId) {
		log.info("SubscriptionController - getDetails");
		return ResponseEntity.ok(service.getDetails(subscriptionId));
	}

	@GetMapping(value = "/complete/{subscriptionId}/{subscriptionDBId}")
	public ResponseEntity<String> complete(@PathVariable String subscriptionId, @PathVariable Long subscriptionDBId) {
		log.info("SubscriptionController - complete");
		return ResponseEntity.ok(service.complete(subscriptionId, subscriptionDBId));
	}

}