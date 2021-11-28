package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.AcquirerResponseDTO;
import com.example.demo.dto.PCCResponseDTO;

@Component
public class ResponseMapper {

	public AcquirerResponseDTO toDTO(PCCResponseDTO pccDto) {
		AcquirerResponseDTO dto = new AcquirerResponseDTO();

		dto.setAuthentificated(pccDto.getAuthentificated());
		dto.setTransactionAutorized(pccDto.getTransactionAutorized());

		return dto;
	}

}
