package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.MerchantDTO;
import com.example.demo.mapper.MerchantMapper;
import com.example.demo.model.Merchant;
import com.example.demo.service.MerchantService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/merchants")
@AllArgsConstructor
public class MerchantController {

	private final MerchantMapper merchantMapper;
	private final MerchantService merchantService;

	@PostMapping
	private ResponseEntity<Merchant> create(@RequestBody MerchantDTO dto) {
		log.info("MerchantController - create");
		Merchant m = merchantMapper.toEntity(dto);
		return new ResponseEntity<>(merchantService.save(m), HttpStatus.CREATED);
	}

	@DeleteMapping("/{merchantId}")
	public ResponseEntity<Void> remove(@PathVariable Integer merchantId) throws NotFoundException {
		log.info("MerchantController - remove: id=" + merchantId);
		merchantService.remove(merchantId);
		return ResponseEntity.noContent().build();
	}

}
