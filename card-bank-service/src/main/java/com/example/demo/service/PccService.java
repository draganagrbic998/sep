package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PccRequest;
import com.example.demo.dto.PccResponse;
import com.example.demo.model.Client;
import com.example.demo.repo.ClientRepository;
import com.example.demo.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class PccService {

	private final ClientRepository clientRepo;
	private final RateService rateService;

	public PccResponse pay(PccRequest request) {
		log.info("PccService - pay: panNumber=" + request.getPanNumber());

		Optional<Client> clientOptional = clientRepo.findByPanNumber(request.getPanNumber());
		if (!clientOptional.isPresent()) {
			log.error("Client: panNumber=" + request.getPanNumber() + " not found.");
			return new PccResponse(false, false);
		}
		Client client = clientOptional.get();

		if (!client.getCardHolder().equals(request.getCardHolder()) || !client.getCvv().equals(request.getCvv())
				|| !client.getExpirationDate().equals(request.getMm() + "/" + request.getYy())) {
			log.error("Client: panNumber=" + request.getPanNumber() + " invalid card data entered.");
			return new PccResponse(false, false);
		}

		if (Utils.cardExpired(client)) {
			log.error("Client: panNumber=" + request.getCardHolder() + " card expired.");
			return new PccResponse(false, false);
		}

		if (request.getAmount() > client.getAvailableFunds()) {
			log.error("Client: panNumber=" + request.getPanNumber() + " not enough available funds.");
			return new PccResponse(true, false);
		}

		client.decAvailableFunds(rateService.getCurrencyRate(request.getCurrency()) * request.getAmount());
		clientRepo.save(client);
		return new PccResponse(true, true);
	}

}
