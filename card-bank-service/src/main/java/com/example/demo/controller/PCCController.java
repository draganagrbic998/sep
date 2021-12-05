package com.example.demo.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PCCRequestDTO;
import com.example.demo.dto.PCCResponseDTO;
import com.example.demo.service.PCCService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/pcc")
@Log4j2
public class PCCController {

	private final PCCService service;

	@PostMapping("/pay")
	public ResponseEntity<PCCResponseDTO> pay(@RequestBody PCCRequestDTO dto) throws IOException {
		log.info("PCCController - pay: acquirerOrderId=" + dto.getAcquirerOrderId());
		return ResponseEntity.ok(service.pay(dto));
	}
}
