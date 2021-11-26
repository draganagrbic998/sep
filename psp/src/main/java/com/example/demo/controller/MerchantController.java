package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.MerchantDTO;
import com.example.demo.mapper.MerchantMapper;
import com.example.demo.model.Merchant;
import com.example.demo.service.MerchantService;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

	@Autowired
	private MerchantMapper merchantMapper;

	@Autowired
	private MerchantService merchantService;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<Merchant> createOrder(@RequestBody MerchantDTO dto) {
		Merchant m = merchantMapper.toEntity(dto);
		return new ResponseEntity<>(merchantService.saveMerchant(m), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{appId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeMerchant(@PathVariable Integer merchantId) throws NotFoundException {
		return new ResponseEntity<>(merchantService.removeMerchant(merchantId), HttpStatus.NO_CONTENT);
	}

}
