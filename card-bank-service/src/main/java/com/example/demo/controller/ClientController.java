package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ClientDTO;
import com.example.demo.mapper.ClientMapper;
import com.example.demo.model.Client;
import com.example.demo.service.ClientService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/clients")
public class ClientController {

	@Autowired
	private ClientMapper mapper;

	@Autowired
	private ClientService service;

	@PostMapping
	public ResponseEntity<Client> create(@RequestBody ClientDTO dto) {
		log.info("ClientController - create");
		return new ResponseEntity<>(service.save(mapper.toEntity(dto)), HttpStatus.CREATED);
	}

}
