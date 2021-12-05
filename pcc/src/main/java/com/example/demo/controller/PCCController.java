package com.example.demo.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.AcquirerResponseDTO;
import com.example.demo.dto.PCCRequestDTO;
import com.example.demo.dto.PCCResponseDTO;
import com.example.demo.mapper.ResponseMapper;
import com.example.demo.service.BankService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/pcc")
@Log4j2
public class PCCController {

	private final ResponseMapper responseMapper;
	private final BankService bankService;
	private final RestTemplate restTemplate;

	@PostMapping("/redirect")
	public ResponseEntity<AcquirerResponseDTO> redirect(@RequestBody PCCRequestDTO dto) {
		log.info("PCCController - redirect: acquirerOrderId=" + dto.getAcquirerOrderId());
		String bankId = dto.getPanNumber().replace("-", "").substring(1, 7);
		String bankUrl = bankService.getBankByPanNumber(bankId).getBankUrl();

		// Saljemo banci kupca
		log.info("redirect - notifying buyer bank @" + bankUrl + "/pcc/pay");
		ResponseEntity<PCCResponseDTO> responseEntity = restTemplate.exchange(bankUrl + "/pcc/pay", HttpMethod.POST,
				new HttpEntity<PCCRequestDTO>(dto), PCCResponseDTO.class);

		return ResponseEntity.ok(responseMapper.toDTO(responseEntity.getBody()));
	}

}
