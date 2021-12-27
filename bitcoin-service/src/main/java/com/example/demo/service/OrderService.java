package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentCompletedDTO;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.PaymentStatus;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.utils.DatabaseCipher;
import com.example.demo.utils.PropertiesData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class OrderService {

	private final OrderRepository repo;
	private final MerchantRepository merchantRepo;
	private final DatabaseCipher cipher;
	private final RestTemplate restTemplate;
	private final PropertiesData properties;

	public Order save(Order order) {
		log.info("OrderService - save: id=" + order.getId());
		return repo.save(cipher.encrypt(order));
	}

	public String getDetails(Long id) {
		try {
			log.info("OrderService - getDetails: id=" + id);

			Order order = cipher.decrypt(repo.findById(id).get());
			Merchant merchant = cipher
					.decrypt(merchantRepo.findByMerchantApiKey(cipher.encrypt(order.getMerchantApiKey())));

			return Payment.get(new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox"),
					order.getPayPalOrderId()).toJSON();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Order createPayment(Long id) {
		log.info("OrderService - createPayment: id=" + id);
		Order order = cipher.decrypt(repo.findById(id).get());
		Merchant merchant = cipher
				.decrypt(merchantRepo.findByMerchantApiKey(cipher.encrypt(order.getMerchantApiKey())));

		Amount amount = new Amount();
		amount.setTotal(order.getPrice().toString());
		amount.setCurrency(order.getCurrency());

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		List<Transaction> transactions = List.of(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setReturnUrl(properties.successUrl);
		redirectUrls.setCancelUrl(properties.cancelUrl);
		payment.setRedirectUrls(redirectUrls);

		try {
			Payment createdPayment = payment
					.create(new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox"));
			if (createdPayment != null) {
				order.setPayPalOrderId(createdPayment.getId());
			} else {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			log.error("createPayment - Error occured during payment transaction");

			order.setStatus(OrderStatus.FAILED);
			order.setExecuted(true);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<>(new PaymentCompletedDTO(PaymentStatus.FAIL)), Void.class);
		}

		merchantRepo.save(cipher.encrypt(merchant));
		return repo.save(cipher.encrypt(order));
	}

	public String completePayment(String paymentId, String payerId) {
		log.info("OrderService - completePayment: paymentId=" + paymentId + ", payerId=" + payerId);
		Order order = cipher.decrypt(repo.findByPayPalOrderId(cipher.encrypt(paymentId)).get());
		Merchant merchant = cipher
				.decrypt(merchantRepo.findByMerchantApiKey(cipher.encrypt(order.getMerchantApiKey())));

		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);
		Payment completedPayment = new Payment();

		try {
			completedPayment = payment.execute(
					new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox"), paymentExecution);
			if (completedPayment != null) {

				order.setStatus(OrderStatus.COMPLETED);
				order.setExecuted(true);

				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.SUCCESS)),
						Void.class);
			} else {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			log.error("completePayment - Error occured during payment execution");

			order.setStatus(OrderStatus.FAILED);
			order.setExecuted(true);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.FAIL)), Void.class);
		}

		merchantRepo.save(cipher.encrypt(merchant));
		repo.save(cipher.encrypt(order));
		return completedPayment.toJSON();
	}

	@Scheduled(fixedDelay = 300000)
	public void checkOrders() {
		log.info("OrderService - checkOrders");

		for (Order order : repo.findAllByExecuted(false)) {
			order = cipher.decrypt(order);
			if (order.getPayPalOrderId() == null) {
				continue;
			}

			Merchant merchant = cipher
					.decrypt(merchantRepo.findByMerchantApiKey(cipher.encrypt(order.getMerchantApiKey())));

			try {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization",
						new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox")
								.fetchAccessToken());

				String status = (new Gson())
						.fromJson(
								restTemplate.exchange(properties.paypalOrdersCheckout + "/" + order.getPayPalOrderId(),
										HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody(),
								JsonObject.class)
						.get("status").getAsString();

				if (status.equalsIgnoreCase("completed")) {
					log.info("Order: id=" + order.getId() + " status=COMPLETED");
					order.setStatus(OrderStatus.COMPLETED);
					order.setExecuted(true);

					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.SUCCESS)),
							Void.class);
				}

			} catch (Exception e) {
				log.error("Order: id=" + order.getId() + " status=FAILED");
				order.setStatus(OrderStatus.FAILED);
				order.setExecuted(true);

				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.FAIL)), Void.class);
			}

			merchantRepo.save(cipher.encrypt(merchant));
			repo.save(cipher.encrypt(order));

		}

	}

}
