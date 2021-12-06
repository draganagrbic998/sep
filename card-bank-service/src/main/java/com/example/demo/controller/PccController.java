package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PccResponse;
import com.example.demo.dto.PccRequest;
import com.example.demo.service.PccService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/pcc")
@Log4j2
public class PccController {

	private final PccService service;

	@PostMapping("/pay")
	public ResponseEntity<PccResponse> pay(@RequestBody PccRequest dto) {
		log.info("PCCController - pay: acquirerOrderId=" + dto.getAcquirerOrderId());
		return ResponseEntity.ok(service.pay(dto));
	}
}
