package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.AcquirerResponseDTO;
import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.PCCRequestDTO;
import com.example.demo.dto.PaymentRequestCompletedDTO;
import com.example.demo.mapper.PCCMapper;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.model.Client;
import com.example.demo.model.PaymentRequest;
import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionStatus;
import com.example.demo.repo.PaymentRequestRepository;
import com.example.demo.utils.Utils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PaymentRequestService {

	@Value("${bankID}")
	private String bankId;

	@Value("${pccURL}")
	private String pccURL;

	@Autowired
	private PaymentRequestRepository paymentRequestRepository;

	@Autowired
	private PCCMapper pccMapper;

	@Autowired
	private ClientService clientService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TransactionMapper transactionMapper;

	@Autowired
	private RestTemplate restTemplate;

	public PaymentRequest findById(Integer id) throws NotFoundException {
		log.info("PaymentRequestService - findById: id=" + id.toString());
		Optional<PaymentRequest> paymentRequest = paymentRequestRepository.findById(id);

		if (!paymentRequest.isPresent()) {
			log.error("PaymentRequest: id=" + id.toString() + " not found.");
			throw new NotFoundException(id.toString(), PaymentRequest.class.getSimpleName());
		}

		return paymentRequest.get();
	}

	public PaymentRequest save(PaymentRequest paymentRequest) {
		log.info("PaymentRequestService - save: id=" + paymentRequest.getId().toString());
		return paymentRequestRepository.save(paymentRequest);
	}

	public String confirmPaymentRequest(ClientDTO clientDTO, Integer paymentRequestId) throws NotFoundException {
		log.info("PaymentRequestService - confirmPaymentRequest: id=" + paymentRequestId.toString());
		// Proveravamo da li je to klijent ove banke
		PaymentRequest paymentRequest = this.findById(paymentRequestId);
		Transaction transaction = transactionMapper.toEntity(paymentRequest);

		String clientBankId = this.getBankIdFromPan(clientDTO.getPanNumber());

		// Jeste klijent ove banke
		if (clientBankId.contentEquals(bankId)) {
			log.info("Client: panNumber=" + clientBankId + "... has an account in this bank");
			Optional<Client> clientOptional = clientService.findClientByPanNumber(clientDTO.getPanNumber());

			if (!clientOptional.isPresent()) {
				log.error("Client: panNumber=" + clientBankId + "... not found");
				transaction.setStatus(TransactionStatus.FAILED);
				this.refusePaymentRequest(paymentRequest);

				return paymentRequest.getFailedUrl();
			}

			Client client = clientOptional.get();
			transaction.setPanNumber(client.getPanNumber());
			String tempDate = clientDTO.getMm() + "/" + clientDTO.getYy();

			if (!client.getCardHolder().equals(clientDTO.getCardHolder()) || !client.getCvv().equals(clientDTO.getCvv())
					|| !client.getExpirationDate().equals(tempDate)) {
				log.error("Client: panNumber=" + clientBankId + "... invalid card data entered");
				transaction.setStatus(TransactionStatus.FAILED);
				this.refusePaymentRequest(paymentRequest);

				return paymentRequest.getFailedUrl();
			}

			// Proveravamo da li je istekla kartica
			if (Utils.cardExpired(client)) {
				log.error("Client: panNumber=" + clientBankId + "... card expired");
				transaction.setStatus(TransactionStatus.FAILED);
				this.refusePaymentRequest(paymentRequest);

				return paymentRequest.getFailedUrl();
			}

			// Proveravamo da li ima dovoljno para na racunu
			if (paymentRequest.getAmount() > client.getAvailableFunds()) {
				log.error("Client: panNumber=" + clientBankId + "... not enough available funds");
				transaction.setStatus(TransactionStatus.FAILED);
				this.refusePaymentRequest(paymentRequest);

				return paymentRequest.getFailedUrl();
			}

			String merchantId = paymentRequest.getMerchantId();
			Client merchant = clientService.getClientByMerchantId(merchantId);

			if (!merchant.getMerchantPassword().equals(paymentRequest.getMerchantPassword())) {
				log.error("Client: panNumber=" + clientBankId + "... invalid Merchant Password");
				transaction.setStatus(TransactionStatus.ERROR);
				this.refusePaymentRequest(paymentRequest);

				return paymentRequest.getErrorUrl();
			}

			// Ovde bi trebalo API od NBS-a da se stavi da prebaci u dinari
			client.setAvailableFunds(client.getAvailableFunds() - paymentRequest.getAmount());
			clientService.save(client);

			// Ovde bi trebalo API od NBS-a da se stavi da prebaci u dinari
			merchant.setAvailableFunds(merchant.getAvailableFunds() + paymentRequest.getAmount());
			clientService.save(merchant);

			transaction.setStatus(TransactionStatus.SUCCESSFUL);
			transactionService.save(transaction);

			PaymentRequestCompletedDTO paymentRequestCompletedDTO = new PaymentRequestCompletedDTO();

			paymentRequestCompletedDTO.setId(paymentRequest.getMerchantOrderId());
			paymentRequestCompletedDTO.setStatus("SUCCESSFUL");

			log.info("confirmPaymentRequest - notifying card-service @" + paymentRequest.getCallbackUrl());
			restTemplate.exchange(paymentRequest.getCallbackUrl(), HttpMethod.POST,
					new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO), String.class);

			return paymentRequest.getSuccessUrl();
		} else {
			log.info("Client: panNumber=" + clientBankId + " doesn't have an account in this bank");

			// Nije klijent ove banke
			transaction = transactionService.save(transaction);

			PCCRequestDTO pccRequestDTO = pccMapper.toDTO(transaction.getId(), clientDTO, paymentRequest);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<PCCRequestDTO> request = new HttpEntity<PCCRequestDTO>(pccRequestDTO, headers);

			String merchantId = paymentRequest.getMerchantId();
			Client merchant = clientService.getClientByMerchantId(merchantId);
			if (!merchant.getMerchantPassword().equals(paymentRequest.getMerchantPassword())) {
				log.error("Client: panNumber=" + clientBankId + "... invalid Merchant Password");
				transaction.setStatus(TransactionStatus.ERROR);
				this.refusePaymentRequest(paymentRequest);
				return paymentRequest.getErrorUrl();
			}

			log.info("confirmPaymentRequest - sending PCCRequestDTO to PCC @" + pccURL + "/redirect");
			AcquirerResponseDTO response = restTemplate
					.exchange(pccURL + "/redirect", HttpMethod.POST, request, AcquirerResponseDTO.class).getBody();

			if (response.getAuthentificated() && response.getTransactionAuthorized()) {
				log.info("AcquirerResponseDTO: authentificated=true transactionAuthorized=true");

				merchant.setAvailableFunds(merchant.getAvailableFunds() + paymentRequest.getAmount());
				clientService.save(merchant);

				transaction.setStatus(TransactionStatus.SUCCESSFUL);
				transactionService.save(transaction);

				PaymentRequestCompletedDTO paymentRequestCompletedDTO = new PaymentRequestCompletedDTO();

				paymentRequestCompletedDTO.setId(paymentRequest.getMerchantOrderId());
				paymentRequestCompletedDTO.setStatus("SUCCESSFUL");

				log.info("confirmPaymentRequest - notifying card-service @" + paymentRequest.getCallbackUrl());
				restTemplate.exchange(paymentRequest.getCallbackUrl(), HttpMethod.POST,
						new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO), String.class);

				return paymentRequest.getSuccessUrl();
			}

			if (!response.getAuthentificated()) {
				log.error("AcquirerResponseDTO: authentificated=false");
				return paymentRequest.getErrorUrl();
			}
			if (!response.getTransactionAuthorized()) {
				log.error("AcquirerResponseDTO: transactionAuthorized=false");
				return paymentRequest.getFailedUrl();
			}

			log.error("AcquirerResponseDTO: authentificated=false transactionAuthorized=false");
			return paymentRequest.getErrorUrl();
		}
	}

	private void refusePaymentRequest(PaymentRequest paymentRequest) {
		log.info("PaymentRequestService - refusePaymentRequest: id=" + paymentRequest.getId().toString());
		PaymentRequestCompletedDTO paymentRequestCompletedDTO = new PaymentRequestCompletedDTO();

		paymentRequestCompletedDTO.setId(paymentRequest.getMerchantOrderId());
		paymentRequestCompletedDTO.setStatus("FAILED");

		log.info("confirmPaymentRequest - notifying card-service @" + paymentRequest.getCallbackUrl());
		restTemplate.exchange(paymentRequest.getCallbackUrl(), HttpMethod.POST,
				new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO), String.class);
	}

	private String getBankIdFromPan(String pan) {
		return pan.replace("-", "").substring(1, 7);
	}

}