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
import com.example.demo.repo.ClientRepository;
import com.example.demo.repo.PaymentRequestRepository;
import com.example.demo.repo.TransactionRepository;
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

	public PaymentRequest save(PaymentRequest request) {
		log.info("PaymentRequestService - save: id=" + request.getId());
		return repo.save(request);
	}

	public String confirm(Long id, Customer customer) {
		log.info("PaymentRequestService - confirm: id=" + id);
		PaymentRequest request = repo.findById(id).get();
		Transaction transaction = new Transaction(request);
		transaction = transactionRepo.save(transaction);

		Optional<Client> merchantOptional = clientRepo.findByMerchantId(request.getMerchantId());
		if (!merchantOptional.isPresent()) {
			log.error("Merchant: id=" + request.getMerchantId() + " not found");
			return refuse(request, transaction, false);
		}
		Client merchant = merchantOptional.get();

		if (!merchant.getMerchantPassword().equals(request.getMerchantPassword())) {
			log.error("Merchant: id=" + request.getMerchantId() + " invalid password");
			return refuse(request, transaction, true);
		}

		if (customer.getPanNumber().replace("-", "").substring(0, 6).contentEquals(properties.bankId)) {
			log.info("Client: panNumber=" + customer.getPanNumber() + " has an account in this bank");

			Optional<Client> clientOptional = clientRepo.findByPanNumber(customer.getPanNumber());
			if (!clientOptional.isPresent()) {
				log.error("Client: panNumber=" + customer.getPanNumber() + " not found");
				return refuse(request, transaction, false);
			}
			Client client = clientOptional.get();

			if (!client.getCardHolder().equals(customer.getCardHolder()) || !client.getCvv().equals(customer.getCvv())
					|| !client.getExpirationDate().equals(customer.getMm() + "/" + customer.getYy())) {
				log.error("Client: panNumber=" + customer.getPanNumber() + " invalid card data entered");
				return refuse(request, transaction, false);
			}

			if (Utils.cardExpired(client)) {
				log.error("Client: panNumber=" + customer.getPanNumber() + " card expired");
				return refuse(request, transaction, false);
			}

			if (request.getAmount() > client.getAvailableFunds()) {
				log.error("Client: panNumber=" + customer.getPanNumber() + " not enough available funds");
				return refuse(request, transaction, false);
			}

			double rate = rateService.findRate(transaction.getCurrency());
			client.decAvailableFunds(rate * request.getAmount());
			clientRepo.save(client);
			merchant.incAvailableFunds(rate * request.getAmount());
			clientRepo.save(merchant);

			log.info("PaymentRequestService - notifying card-service @" + request.getCallbackUrl());
			restTemplate.exchange(request.getCallbackUrl(), HttpMethod.POST,
					new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.SUCCESS)),
					String.class);
			return request.getSuccessUrl();
		} else {
			log.info("Client: panNumber=" + customer.getPanNumber() + " doesn't have an account in this bank");
			log.info("PaymentRequestService - sending PccRequest to PCC @" + properties.pccURL);

			PccRequest pccRequest = new PccRequest(request, customer);

			PccResponse response = restTemplate.exchange(properties.pccURL, HttpMethod.POST,
					new HttpEntity<PccRequest>(pccRequest), PccResponse.class).getBody();

			if (response.getAuthenticated() && response.getTransactionAuthorized()) {
				log.info("PccResponse: authentificated=true transactionAuthorized=true");
				log.info("PaymentRequestService - notifying card-service @" + request.getCallbackUrl());

				merchant.incAvailableFunds(rateService.findRate(request.getCurrency()) * request.getAmount());
				clientRepo.save(merchant);

				restTemplate.exchange(request.getCallbackUrl(), HttpMethod.POST,
						new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.SUCCESS)),
						String.class);
				return request.getSuccessUrl();
			}

			if (!response.getAuthenticated()) {
				log.error("PccResponse: authentificated=false");
				return request.getErrorUrl();
			}
			if (!response.getTransactionAuthorized()) {
				log.error("PccResponse: transactionAuthorized=false");
				return request.getFailUrl();
			}

			log.error("PccResponse: authentificated=false transactionAuthorized=false");
			return request.getErrorUrl();
		}
	}

	private String refuse(PaymentRequest request, Transaction transaction, boolean error) {
		log.info("PaymentRequestService - refuse: id=" + request.getId());
		log.info("PaymentRequestService - notifying card-service @" + request.getCallbackUrl());
		transaction.setStatus(error ? PaymentStatus.ERROR : PaymentStatus.FAIL);
		transactionRepo.save(transaction);
		restTemplate.exchange(request.getCallbackUrl(), HttpMethod.POST,
				new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.FAIL)), String.class);
		return error ? request.getErrorUrl() : request.getFailUrl();
	}

}
