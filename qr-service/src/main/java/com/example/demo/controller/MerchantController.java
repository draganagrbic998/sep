package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Merchant;
import com.example.demo.service.MerchantService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/merchants")
@Log4j2
public class MerchantController {

	private final MerchantService service;

	@PostMapping
	public ResponseEntity<Merchant> create(@RequestBody Merchant dto) {
		log.info("MerchantController - create");
		return ResponseEntity.ok(service.save(dto));
	}
}
