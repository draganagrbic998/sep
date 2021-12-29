package com.example.demo.service;

import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.Customer;
import com.example.demo.dto.PaymentRequestCompleted;
import com.example.demo.dto.PccRequest;
import com.example.demo.dto.PccResponse;
import com.example.demo.model.Client;
import com.example.demo.model.PaymentRequest;
import com.example.demo.model.PaymentStatus;
import com.example.demo.model.Transaction;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.PaymentRequestRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.utils.DatabaseCipher;
import com.example.demo.utils.PropertiesData;
import com.example.demo.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class PaymentRequestService {

	private final PaymentRequestRepository repo;
	private final ClientRepository clientRepo;
	private final TransactionRepository transactionRepo;
	private final RateService rateService;
	private final RestTemplate restTemplate;
	private final PropertiesData properties;
	private final DatabaseCipher cipher;

	public PaymentRequest save(PaymentRequest request) {
		log.info("PaymentRequestService - save: id=" + request.getId());
		return repo.save(cipher.encrypt(request));
	}

	public String confirm(Long id, Customer customer) {
		log.info("PaymentRequestService - confirm: id=" + id);
		PaymentRequest request = repo.findById(id).get();
		Transaction transaction = transactionRepo.save(new Transaction(request));

		Optional<Client> merchantOptional = clientRepo.findByMerchantId(request.getMerchantId());
		if (!merchantOptional.isPresent()) {
			log.error("Merchant: id=" + cipher.decrypt(request.getMerchantId()) + " not found.");
			return refuse(request, transaction, false);
		}
		Client merchant = merchantOptional.get();

		if (!merchant.getMerchantPassword().equals(request.getMerchantPassword())) {
			log.error("Merchant: id=" + cipher.decrypt(request.getMerchantId()) + " invalid password.");
			return refuse(request, transaction, true);
		}

		if (customer.getPanNumber().substring(0, 6).equals(properties.bankId)) {
			log.info("Client: panNumber=" + customer.getPanNumber() + " has an account in this bank.");

			Optional<Client> clientOptional = clientRepo.findByPanNumber(cipher.encrypt(customer.getPanNumber()));
			if (!clientOptional.isPresent()) {
				log.error("Client: panNumber=" + customer.getPanNumber() + " not found.");
				return refuse(request, transaction, false);
			}
			Client client = clientOptional.get();

			if (!cipher.decrypt(client.getCardHolder()).equals(customer.getCardHolder())
					|| !cipher.decrypt(client.getCvv()).equals(customer.getCvv())
					|| !cipher.decrypt(client.getExpirationDate()).equals(customer.getMm() + "/" + customer.getYy())) {
				log.error("Client: panNumber=" + customer.getPanNumber() + " invalid card data entered.");
				return refuse(request, transaction, false);
			}

			if (Utils.cardExpired(cipher.decrypt(client.getExpirationDate()))) {
				log.error("Client: panNumber=" + customer.getPanNumber() + " card expired.");
				return refuse(request, transaction, false);
			}

			if (request.getAmount() > client.getAvailableFunds()) {
				log.error("Client: panNumber=" + customer.getPanNumber() + " not enough available funds.");
				return refuse(request, transaction, false);
			}

			Double rate = rateService.getCurrencyRate(transaction.getCurrency());
			merchant.incAvailableFunds(rate * request.getAmount());
			clientRepo.save(merchant);
			client.decAvailableFunds(rate * request.getAmount());
			clientRepo.save(client);

			log.info("PaymentRequestService - notifying card-service @" + request.getCallbackUrl());
			restTemplate.exchange(request.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.SUCCESS)),
					Void.class);
			return request.getSuccessUrl();
		} else {
			log.info("Client: panNumber=" + customer.getPanNumber() + " doesn't have an account in this bank.");
			log.info("PaymentRequestService - sending PccRequest to PCC @" + properties.pccURL);

			PccResponse response = restTemplate.exchange(properties.pccURL, HttpMethod.POST,
					new HttpEntity<PccRequest>(new PccRequest(request, customer)), PccResponse.class).getBody();

			if (response.getAuthenticated() && response.getTransactionAuthorized()) {
				log.info("PccResponse: authenticated=true, transactionAuthorized=true");
				merchant.incAvailableFunds(rateService.getCurrencyRate(request.getCurrency()) * request.getAmount());
				clientRepo.save(merchant);

				log.info("PaymentRequestService - notifying card-service @" + request.getCallbackUrl());
				restTemplate.exchange(request.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.SUCCESS)),
						Void.class);
				return request.getSuccessUrl();
			}

			if (!response.getAuthenticated()) {
				log.error("PccResponse: authenticated=false");
				return request.getFailUrl();
			}
			if (!response.getTransactionAuthorized()) {
				log.error("PccResponse: transactionAuthorized=false");
				return request.getFailUrl();
			}
			log.error("PccResponse: authenticated=false, transactionAuthorized=false");
			return request.getErrorUrl();
		}
	}

	private String refuse(PaymentRequest request, Transaction transaction, boolean error) {
		log.info("PaymentRequestService - refuse: id=" + request.getId());
		log.info("PaymentRequestService - notifying card-service @" + request.getCallbackUrl());
		transaction.setStatus(error ? PaymentStatus.ERROR : PaymentStatus.FAIL);
		transactionRepo.save(transaction);
		restTemplate.exchange(request.getCallbackUrl(), HttpMethod.PUT,
				new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.FAIL)), Void.class);
		return error ? request.getErrorUrl() : request.getFailUrl();
	}

}
