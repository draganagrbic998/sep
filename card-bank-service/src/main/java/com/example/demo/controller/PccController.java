package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PccRequest;
import com.example.demo.dto.PccResponse;
import com.example.demo.service.PccService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/pcc")
public class PccController {

	private final PccService service;

	@PostMapping
	public ResponseEntity<PccResponse> pay(@RequestBody PccRequest dto) {
		return ResponseEntity.ok(service.pay(dto));
	}
}
