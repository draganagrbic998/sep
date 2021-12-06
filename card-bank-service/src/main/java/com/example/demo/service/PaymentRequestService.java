package com.example.demo.service;

import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.PaymentRequestCompleted;
import com.example.demo.dto.PccRequest;
import com.example.demo.dto.PccResponse;
import com.example.demo.model.Client;
import com.example.demo.model.PaymentRequest;
import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionStatus;
import com.example.demo.repo.PaymentRequestRepository;
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
	private final ClientService clientService;
	private final TransactionService transactionService;
	private final RateService rateService;
	private final RestTemplate restTemplate;
	private final DatabaseCipher cipher;
	private final PropertiesData data;

	public PaymentRequest save(PaymentRequest request) {
		log.info("PaymentRequestService - save: id=" + request.getId());
		return repo.save(cipher.encrypt(request));
	}

	public String confirm(Long id, ClientDTO clientDTO) {
		log.info("PaymentRequestService - confirmPaymentRequest: id=" + id);
		PaymentRequest request = findById(id);
		Transaction transaction = new Transaction(request);
		String clientBankId = clientDTO.getPanNumber().replace("-", "").substring(0, 6);

		if (clientBankId.contentEquals(data.bankId)) {
			log.info("Client: panNumber=" + clientBankId + "... has an account in this bank");
			Optional<Client> clientOptional = clientService
					.findClientByPanNumber(cipher.encrypt(clientDTO.getPanNumber()));

			if (!clientOptional.isPresent()) {
				log.error("Client: panNumber=" + clientBankId + "... not found");
				return refuse(request, transaction, false);
			}

			Client client = cipher.decrypt(clientOptional.get());
			// transaction.setPanNumber(client.getPanNumber());

			if (!client.getCardHolder().equals(clientDTO.getCardHolder()) || !client.getCvv().equals(clientDTO.getCvv())
					|| !client.getExpirationDate().equals(clientDTO.getMm() + "/" + clientDTO.getYy())) {
				log.error("Client: panNumber=" + clientBankId + "... invalid card data entered");
				return refuse(request, transaction, false);
			}

			if (Utils.cardExpired(client)) {
				log.error("Client: panNumber=" + clientBankId + "... card expired");
				return refuse(request, transaction, false);
			}

			if (request.getAmount() > client.getAvailableFunds()) {
				log.error("Client: panNumber=" + clientBankId + "... not enough available funds");
				return refuse(request, transaction, false);
			}

			client = cipher.encrypt(client);

			String merchantId = request.getMerchantId();
			Client merchant = clientService.getClientByMerchantId(merchantId);

			if (!merchant.getMerchantPassword().equals(request.getMerchantPassword())) {
				log.error("Client: panNumber=" + clientBankId + "... invalid Merchant Password");
				return refuse(request, transaction, true);
			}

			double rate = rateService.findRate(transaction.getCurrency());

			client.decAvailableFunds(rate * request.getAmount());
			clientService.save(client);

			merchant.incAvailableFunds(rate * request.getAmount());
			clientService.save(merchant);

			transaction.setStatus(TransactionStatus.SUCCESS);
			transactionService.save(transaction);

			log.info("confirmPaymentRequest - notifying card-service @" + request.getCallbackUrl());
			restTemplate.exchange(request.getCallbackUrl(), HttpMethod.POST,
					new HttpEntity<PaymentRequestCompleted>(
							new PaymentRequestCompleted(request.getMerchantOrderId(), TransactionStatus.SUCCESS)),
					String.class);

			return request.getSuccessUrl();
		} else {
			log.info("Client: panNumber=" + clientBankId + " doesn't have an account in this bank");

			transaction = transactionService.save(transaction);
			PccRequest pccRequest = new PccRequest(request, clientDTO, transaction.getId());

			// HttpHeaders headers = new HttpHeaders();
			// headers.setContentType(MediaType.APPLICATION_JSON);
			// HttpEntity<PccRequest> request = new HttpEntity<PccRequest>(pccRequest,
			// headers);

			Client merchant = clientService.getClientByMerchantId(request.getMerchantId());
			if (!merchant.getMerchantPassword().equals(request.getMerchantPassword())) {
				log.error("Client: panNumber=" + clientBankId + "... invalid Merchant Password");
				return refuse(request, transaction, true);
			}

			log.info("confirmPaymentRequest - sending PCCRequestDTO to PCC @" + data.pccURL + "/redirect");
			PccResponse response = restTemplate.exchange(data.pccURL + "/redirect", HttpMethod.POST,
					new HttpEntity<PccRequest>(pccRequest), PccResponse.class).getBody();

			if (response.getAuthentificated() && response.getTransactionAuthorized()) {
				log.info("AcquirerResponseDTO: authentificated=true transactionAuthorized=true");

				merchant.incAvailableFunds(rateService.findRate(request.getCurrency()) * request.getAmount());
				// pre nije bilo ovo mnozenje sa currency
				clientService.save(merchant);

				transaction.setStatus(TransactionStatus.SUCCESS);
				transactionService.save(transaction);

				log.info("confirmPaymentRequest - notifying card-service @" + request.getCallbackUrl());
				restTemplate.exchange(request.getCallbackUrl(), HttpMethod.POST,
						new HttpEntity<PaymentRequestCompleted>(
								new PaymentRequestCompleted(request.getMerchantOrderId(), TransactionStatus.SUCCESS)),
						String.class);
				return request.getSuccessUrl();
			}

			if (!response.getAuthentificated()) {
				log.error("AcquirerResponseDTO: authentificated=false");
				return request.getErrorUrl();
			}
			if (!response.getTransactionAuthorized()) {
				log.error("AcquirerResponseDTO: transactionAuthorized=false");
				return request.getFailUrl();
			}

			log.error("AcquirerResponseDTO: authentificated=false transactionAuthorized=false");
			return request.getErrorUrl();
		}
	}

	private String refuse(PaymentRequest request, Transaction transaction, boolean error) {
		log.info("PaymentRequestService - refuse: id=" + request.getId());
		log.info("confirmPaymentRequest - notifying card-service @" + request.getCallbackUrl());
		transaction.setStatus(error ? TransactionStatus.ERROR : TransactionStatus.FAIL);
		transactionService.save(transaction);
		restTemplate.exchange(request.getCallbackUrl(), HttpMethod.POST,
				new HttpEntity<PaymentRequestCompleted>(
						new PaymentRequestCompleted(request.getMerchantOrderId(), TransactionStatus.FAIL)),
				String.class);
		return error ? request.getErrorUrl() : request.getFailUrl();
	}

	private PaymentRequest findById(Long id) {
		log.info("PaymentRequestService - findById: id=" + id);
		Optional<PaymentRequest> paymentRequest = repo.findById(id);

		if (!paymentRequest.isPresent()) {
			log.error("PaymentRequest: id=" + id + " not found.");
			throw new NotFoundException(id.toString(), PaymentRequest.class.getSimpleName());
		}

		return cipher.decrypt(paymentRequest.get());
	}

}
