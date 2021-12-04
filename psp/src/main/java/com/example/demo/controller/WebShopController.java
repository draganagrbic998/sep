package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.WebShop;
import com.example.demo.service.WebShopService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/webshops")
@Log4j2
public class WebShopController {

	private final WebShopService service;

	@GetMapping
	public ResponseEntity<List<WebShop>> read() {
		log.info("WebShopController - read");
		return ResponseEntity.ok(service.read());
	}

}
