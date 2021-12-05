package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Currency;
import com.example.demo.service.CurrencyService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/currencies")
@Log4j2
public class CurrencyController {

	private final CurrencyService service;

	@GetMapping
	public ResponseEntity<List<Currency>> read() {
		log.info("CurrencyController - read");
		return ResponseEntity.ok(service.read());
	}

}
