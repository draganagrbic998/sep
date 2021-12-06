package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PccRequest;
import com.example.demo.dto.PccResponse;
import com.example.demo.model.Client;
import com.example.demo.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class PccService {

	private final ClientService clientService;
	private final RateService rateService;

	public PccResponse pay(PccRequest request) {
		log.info("PCCService - pay: acquirerOrderId=" + request.getAcquirerOrderId());
		String clientBankId = request.getPanNumber().replace("-", "").substring(0, 6);
		Optional<Client> clientOptional = clientService.findClientByPanNumber(request.getPanNumber());

		if (!clientOptional.isPresent()) {
			log.error("Client: panNumber=" + clientBankId + "... not found.");
			return new PccResponse(false, false);
		}

		Client client = clientOptional.get();
		if (!client.getCardHolder().equals(request.getCardHolder()) || !client.getCvv().equals(request.getCvv())
				|| !client.getExpirationDate().equals(request.getMm() + "/" + request.getYy())) {
			log.error("Client: panNumber=" + clientBankId + "... invalid card data entered");
			return new PccResponse(false, false);
		}

		if (Utils.cardExpired(client)) {
			log.error("Client: panNumber=" + clientBankId + "... card expired");
			return new PccResponse(false, false);
		}

		if (request.getAmount() > client.getAvailableFunds()) {
			log.error("Client: panNumber=" + clientBankId + "... not enough available funds");
			return new PccResponse(true, false);
		}

		client.decAvailableFunds(rateService.findRate(request.getCurrency()) * request.getAmount());
		clientService.save(client);
		return new PccResponse(true, true);
	}

}
