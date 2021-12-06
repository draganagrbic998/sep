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

	@PostMapping("/redirect")
	public ResponseEntity<PccResponse> redirect(@RequestBody PccRequest dto) {
		log.info("PCCController - redirect: acquirerOrderId=" + dto.getAcquirerOrderId());
		// log.info("redirect - notifying buyer bank @" + bankUrl + "/pcc/pay");
		return ResponseEntity.ok(restTemplate
				.exchange(
						bankService.getBankByPanNumber(dto.getPanNumber().replace("-", "").substring(0, 6)).getBankUrl()
								+ "/pcc/pay",
						HttpMethod.POST, new HttpEntity<PccRequest>(dto), PccResponse.class)
				.getBody());
	}

}
