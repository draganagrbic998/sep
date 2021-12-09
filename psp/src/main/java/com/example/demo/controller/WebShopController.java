package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.WebShop;
import com.example.demo.service.WebShopService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/webshops")
@PreAuthorize("hasAuthority('psp-admin')")
public class WebShopController {

	private final WebShopService service;

	@GetMapping
	public ResponseEntity<List<WebShop>> read() {
		return ResponseEntity.ok(service.read());
	}

}
