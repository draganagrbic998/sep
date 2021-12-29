package com.example.demo.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PccRequest;
import com.example.demo.dto.PccResponse;
import com.example.demo.service.BankService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/pcc")
@Log4j2
public class PccController {

	private final BankService bankService;
	private final RestTemplate restTemplate;

	@PostMapping
	public ResponseEntity<PccResponse> redirect(@RequestBody PccRequest dto) {
		String bankUrl = bankService.findByPanNumber(dto.getPanNumber().substring(0, 6)).getUrl();
		log.info("PccController - redirect: notifying buyer bank @" + bankUrl);
		return ResponseEntity.ok(restTemplate
				.exchange(bankUrl + "/pcc", HttpMethod.POST, new HttpEntity<>(dto), PccResponse.class)
				.getBody());
	}

}
