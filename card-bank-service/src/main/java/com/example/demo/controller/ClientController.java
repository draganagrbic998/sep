package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ClientDTO;
import com.example.demo.mapper.ClientMapper;
import com.example.demo.model.Client;
import com.example.demo.service.ClientService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/clients")
@Log4j2
public class ClientController {

	private final ClientMapper mapper;
	private final ClientService service;

	@PostMapping
	public ResponseEntity<Client> create(@RequestBody ClientDTO dto) {
		log.info("ClientController - create");
		return ResponseEntity.ok(service.save(mapper.toEntity(dto)));
	}

}
