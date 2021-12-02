package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PCCRequestDTO;
import com.example.demo.service.PCCService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/pcc")
public class PCCController {

	@Autowired
	private PCCService pccService;

	@RequestMapping(value = "/pay", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> pay(@RequestBody PCCRequestDTO pccRequestDTO) {
		log.info("PCCController - pay: acquirerOrderId=" + pccRequestDTO.getAcquirerOrderId().toString());
		return new ResponseEntity<>(pccService.pay(pccRequestDTO), HttpStatus.OK);
	}
}
