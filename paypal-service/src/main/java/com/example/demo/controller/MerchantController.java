package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.MerchantDTO;
import com.example.demo.mapper.MerchantMapper;
import com.example.demo.model.Merchant;
import com.example.demo.service.MerchantService;

@RestController
@RequestMapping(value = "/paypal")
public class MerchantController {

	@Autowired
	private MerchantMapper mapper;

	@Autowired
	private MerchantService service;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Merchant> create(@RequestBody MerchantDTO dto) {
		return new ResponseEntity<>(service.save(mapper.toEntity(dto)), HttpStatus.CREATED);
	}
}