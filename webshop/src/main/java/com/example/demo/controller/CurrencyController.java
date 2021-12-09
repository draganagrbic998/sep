package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Currency;
import com.example.demo.service.CurrencyService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/currencies")
@PreAuthorize("hasAuthority('merchant')")
public class CurrencyController {

	private final CurrencyService service;

	@GetMapping
	public ResponseEntity<List<Currency>> read() {
		return ResponseEntity.ok(service.read());
	}

}
