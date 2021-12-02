package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.AcquirerResponseDTO;
import com.example.demo.dto.PCCResponseDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ResponseMapper {

	public AcquirerResponseDTO toDTO(PCCResponseDTO pccDto) {
		log.info("ResponseMapper - toDTO: acquirerOrderId=" + pccDto.getAcquirerOrderId());
		AcquirerResponseDTO dto = new AcquirerResponseDTO();

		dto.setAuthentificated(pccDto.getAuthentificated());
		dto.setTransactionAutorized(pccDto.getTransactionAutorized());

		return dto;
	}

}
