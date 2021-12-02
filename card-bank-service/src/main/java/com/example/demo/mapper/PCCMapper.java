package com.example.demo.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.PCCRequestDTO;
import com.example.demo.dto.PCCResponseDTO;
import com.example.demo.model.PaymentRequest;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class PCCMapper {

	public PCCRequestDTO toDTO(Integer transactionId, ClientDTO clientDTO, PaymentRequest paymentRequest) {
		log.info("PCCMapper - toDTO: transactionId=" + transactionId.toString());
		PCCRequestDTO dto = new PCCRequestDTO();

		dto.setAcquirerOrderId(transactionId);
		dto.setAcquirerTimestamp(LocalDateTime.now());
		dto.setCardHolder(clientDTO.getCardHolder());
		dto.setCvv(clientDTO.getCvv());
		dto.setMm(clientDTO.getMm());
		dto.setPanNumber(clientDTO.getPanNumber());
		dto.setYy(clientDTO.getYy());
		dto.setAmount(paymentRequest.getAmount());
		dto.setCurrency(paymentRequest.getCurrency());

		return dto;
	}

	public PCCResponseDTO toSuccessfulPCCResponse(PCCRequestDTO pccRequestDTO) {
		log.info("PCCMapper - toSuccessfulPCCResponse: acquirerOrderId="
				+ pccRequestDTO.getAcquirerOrderId().toString());
		PCCResponseDTO responseDTO = new PCCResponseDTO();

		responseDTO.setAcquirerOrderId(pccRequestDTO.getAcquirerOrderId());
		responseDTO.setAcquirerTimestamp(pccRequestDTO.getAcquirerTimestamp());

		responseDTO.setAuthentificated(true);
		responseDTO.setTransactionAutorized(true);

		return responseDTO;
	}

	public PCCResponseDTO toFailedAuthPCCResponse(PCCRequestDTO pccRequestDTO) {
		log.info("PCCMapper - toFailedAuthPCCResponse: acquirerOrderId="
				+ pccRequestDTO.getAcquirerOrderId().toString());
		PCCResponseDTO responseDTO = new PCCResponseDTO();

		responseDTO.setAcquirerOrderId(pccRequestDTO.getAcquirerOrderId());
		responseDTO.setAcquirerTimestamp(pccRequestDTO.getAcquirerTimestamp());

		responseDTO.setAuthentificated(false);
		responseDTO.setTransactionAutorized(false);

		return responseDTO;
	}

	public PCCResponseDTO toFailedPaymentPCCResponse(PCCRequestDTO pccRequestDTO) {
		log.info("PCCMapper - toFailedPaymentPCCResponse: acquirerOrderId="
				+ pccRequestDTO.getAcquirerOrderId().toString());
		PCCResponseDTO responseDTO = new PCCResponseDTO();

		responseDTO.setAcquirerOrderId(pccRequestDTO.getAcquirerOrderId());
		responseDTO.setAcquirerTimestamp(pccRequestDTO.getAcquirerTimestamp());

		responseDTO.setAuthentificated(true);
		responseDTO.setTransactionAutorized(false);

		return responseDTO;
	}

}
