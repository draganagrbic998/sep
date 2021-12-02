package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.PCCRequestDTO;
import com.example.demo.dto.PCCResponseDTO;
import com.example.demo.mapper.ResponseMapper;
import com.example.demo.service.BankService;

@RestController
@RequestMapping("/pcc")
public class PCCController {

	@Autowired
	private ResponseMapper responseMapper;

	@Autowired
	private BankService bankService;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/redirect", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> redirect(@RequestBody PCCRequestDTO pccRequestDTO) throws NotFoundException {
		String bankId = pccRequestDTO.getPanNumber().replace("-", "").substring(1, 7);
		String bankUrl = bankService.getBankByPanNumber(bankId).getBankUrl();

		// Saljemo banci kupca
		ResponseEntity<PCCResponseDTO> responseEntity = restTemplate.exchange(bankUrl + "/pcc/pay", HttpMethod.POST,
				new HttpEntity<PCCRequestDTO>(pccRequestDTO), PCCResponseDTO.class);

		// Saljemo banci prodavca
		return new ResponseEntity<>(responseMapper.toDTO(responseEntity.getBody()), HttpStatus.OK);
	}

}
