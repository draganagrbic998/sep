package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PCCRequestDTO;
import com.example.demo.dto.PCCResponseDTO;
import com.example.demo.mapper.PCCMapper;
import com.example.demo.model.Client;
import com.example.demo.utils.DatabaseCipher;
import com.example.demo.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class PCCService {

	private final PCCMapper pccMapper;
	private final ClientService clientService;
	private final DatabaseCipher cipher;
	private final KursnaListaService rateService;

	public PCCResponseDTO pay(PCCRequestDTO pccRequestDTO) throws IOException {
		log.info("PCCService - pay: acquirerOrderId=" + pccRequestDTO.getAcquirerOrderId());

		String clientBankId = pccRequestDTO.getPanNumber().replace("-", "").substring(1, 7);

		Optional<Client> clientOptional = clientService.findClientByPanNumber(pccRequestDTO.getPanNumber());

		if (!clientOptional.isPresent()) {
			log.error("Client: panNumber=" + clientBankId + "... not found.");
			return pccMapper.toFailedAuthPCCResponse(pccRequestDTO);
		}

		Client client = cipher.decrypt(clientOptional.get());

		String tempDate = pccRequestDTO.getMm() + "/" + pccRequestDTO.getYy();

		if (!client.getCardHolder().equals(pccRequestDTO.getCardHolder())
				|| !client.getCvv().equals(pccRequestDTO.getCvv()) || !client.getExpirationDate().equals(tempDate)) {
			log.error("Client: panNumber=" + clientBankId + "... invalid card data entered");
			return pccMapper.toFailedAuthPCCResponse(pccRequestDTO);
		}

		// Proveravamo da li je istekla kartica
		if (Utils.cardExpired(client)) {
			log.error("Client: panNumber=" + clientBankId + "... card expired");
			return pccMapper.toFailedAuthPCCResponse(pccRequestDTO);
		}

		// Proveravamo da li ima dovoljno para na racunu
		if (pccRequestDTO.getAmount() > client.getAvailableFunds()) {
			log.error("Client: panNumber=" + clientBankId + "... not enough available funds");
			return pccMapper.toFailedPaymentPCCResponse(pccRequestDTO);
		}

		double rate = rateService.findRate(pccRequestDTO.getCurrency().toLowerCase(),
				DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.now())).getValue().doubleValue();

		client.setAvailableFunds(client.getAvailableFunds() - (pccRequestDTO.getAmount() * rate));
		clientService.save(client);

		return pccMapper.toSuccessfulPCCResponse(pccRequestDTO);
	}

}
