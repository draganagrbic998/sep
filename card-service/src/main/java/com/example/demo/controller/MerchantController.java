package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.MerchantDTO;
import com.example.demo.mapper.MerchantMapper;
import com.example.demo.model.Merchant;
import com.example.demo.service.MerchantService;

@RestController
@RequestMapping(value = "/merchant")
public class MerchantController {

	@Autowired
	private MerchantMapper mapper;

	@Autowired
	private MerchantService service;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Merchant create(@RequestBody MerchantDTO dto)
			throws NotFoundException {
		return service.save(mapper.toEntity(dto));
	}
}