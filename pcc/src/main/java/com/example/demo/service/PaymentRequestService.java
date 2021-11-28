package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.PaymentRequestCompletedDTO;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.model.Client;
import com.example.demo.model.PaymentRequest;
import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionStatus;
import com.example.demo.repo.PaymentRequestRepository;

@Service
public class PaymentRequestService {

	@Value("${bankId}")
	private String bankId;

	@Autowired
	private PaymentRequestRepository paymentRequestRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TransactionMapper transactionMapper;

	@Autowired
	private RestTemplate restTemplate;

	public PaymentRequest save(PaymentRequest paymentRequest) {
		return paymentRequestRepository.save(paymentRequest);
	}

	public PaymentRequest getPaymentRequest(Integer id) throws NotFoundException {
		Optional<PaymentRequest> paymentRequest = paymentRequestRepository.findById(id);

		if (!paymentRequest.isPresent()) {
			throw new NotFoundException(id.toString(), PaymentRequest.class.getSimpleName());
		}

		return paymentRequest.get();
	}

	public String confirmPaymentRequest(ClientDTO clientDTO, Integer paymentRequestId) throws NotFoundException {
		// Proveravamo da li je to klijent ove banke
		PaymentRequest paymentRequest = this.getPaymentRequest(paymentRequestId);
		Transaction transaction = transactionMapper.toEntity(paymentRequest);

		// Jeste klijent ove banke
		if (this.getBankIdFromPan(clientDTO.getPanNumber()).contentEquals(bankId)) {
			Optional<Client> clientOpt = clientService.findClientByPanNumber(clientDTO.getPanNumber());

			if (!clientOpt.isPresent()) {
				transaction.setStatus(TransactionStatus.FAILED);
				this.refusePaymentRequest(paymentRequest);

				return paymentRequest.getFailedUrl();
			}

			Client client = clientOpt.get();
			transaction.setPanNumber(client.getPanNumber());
			String tempDate = clientDTO.getMm() + "/" + clientDTO.getYy();

			if (!client.getCardHolder().equals(clientDTO.getCardHolder()) || !client.getCvv().equals(clientDTO.getCvv())
					|| !client.getExpirationDate().equals(tempDate)) {
				transaction.setStatus(TransactionStatus.FAILED);
				this.refusePaymentRequest(paymentRequest);

				return paymentRequest.getFailedUrl();
			}

			if (paymentRequest.getAmount() > client.getAvailableFunds()) {
				transaction.setStatus(TransactionStatus.FAILED);
				this.refusePaymentRequest(paymentRequest);

				return paymentRequest.getFailedUrl();
			}

			String merchantId = paymentRequest.getMerchantId();
			Client merchant = clientService.getClientByMerchantId(merchantId);

			if (!merchant.getMerchantPassword().equals(paymentRequest.getMerchantPassword())) {
				transaction.setStatus(TransactionStatus.ERROR);
				this.refusePaymentRequest(paymentRequest);

				return paymentRequest.getErrorUrl();
			}

			client.setAvailableFunds(client.getAvailableFunds() - paymentRequest.getAmount());
			clientService.save(client);

			merchant.setAvailableFunds(merchant.getAvailableFunds() + paymentRequest.getAmount());
			clientService.save(merchant);

			transaction.setStatus(TransactionStatus.SUCCESSFUL);
			transactionService.save(transaction);

			PaymentRequestCompletedDTO paymentRequestCompletedDTO = new PaymentRequestCompletedDTO();

			paymentRequestCompletedDTO.setMerchantOrderId(paymentRequest.getMerchantOrderId());
			paymentRequestCompletedDTO.setStatus("SUCCESSFUL");

			restTemplate.exchange(paymentRequest.getCallbackUrl() + "/complete", HttpMethod.POST,
					new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO), String.class);

			return paymentRequest.getSuccessUrl();
		} else {
			// Nije klijent ove banke
			return "";
		}
	}

	private void refusePaymentRequest(PaymentRequest paymentRequest) {
		PaymentRequestCompletedDTO paymentRequestCompletedDTO = new PaymentRequestCompletedDTO();

		paymentRequestCompletedDTO.setMerchantOrderId(paymentRequest.getMerchantOrderId());
		paymentRequestCompletedDTO.setStatus("FAILED");

		restTemplate.exchange(paymentRequest.getCallbackUrl() + "/complete", HttpMethod.POST,
				new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO), String.class);
	}

	private String getBankIdFromPan(String pan) {
		return pan.replace("-", "").substring(1, 7);
	}
}
