package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.OrderStatusUpdate;
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
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

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

	public String getDetails(Long id) throws PayPalRESTException {
		log.info("OrderService - getDetails: id=" + id);
		Order order = repo.findById(id).get();
		Merchant merchant = merchantRepo.findByMerchantApiKey(order.getMerchantApiKey());

		return Payment.get(new APIContext(cipher.decrypt(merchant.getClientId()),
				cipher.decrypt(merchant.getClientSecret()), "sandbox"), cipher.decrypt(order.getPayPalOrderId()))
				.toJSON();
	}

	public Order create(Long id) {
		log.info("OrderService - create: id=" + id);
		Order order = repo.findById(id).get();
		Merchant merchant = merchantRepo.findByMerchantApiKey(order.getMerchantApiKey());

		Amount amount = new Amount();
		amount.setCurrency(order.getCurrency());
		amount.setTotal(order.getPrice().toString());

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
			order.setPayPalOrderId(cipher.encrypt(payment.create(new APIContext(cipher.decrypt(merchant.getClientId()),
					cipher.decrypt(merchant.getClientSecret()), "sandbox")).getId()));
		} catch (Exception e) {
			log.error("create - Error occured during payment execution");

			order.setStatus(OrderStatus.FAILED);
			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.FAIL)), Void.class);

		}

		return repo.save(order);
	}

	public String complete(String paymentId, String payerId) {
		log.info("OrderService - complete: paymentId=" + paymentId + ", payerId=" + payerId);
		Order order = repo.findByPayPalOrderId(cipher.encrypt(paymentId)).get();
		Merchant merchant = merchantRepo.findByMerchantApiKey(order.getMerchantApiKey());

		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);

		try {
			if (payment.execute(new APIContext(cipher.decrypt(merchant.getClientId()),
					cipher.decrypt(merchant.getClientSecret()), "sandbox"), paymentExecution) != null) {

				order.setStatus(OrderStatus.COMPLETED);
				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.SUCCESS)), Void.class);

			} else {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			log.error("complete - Error occured during payment execution");

			order.setStatus(OrderStatus.FAILED);
			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.FAIL)), Void.class);

		}

		return repo.save(order).getStatus().toString();
	}

	@Scheduled(fixedDelay = 300000)
	public void checkOrders() {
		log.info("OrderService - checkOrders");

		for (Order order : repo.findAll()) {
			if (!order.getStatus().equals(OrderStatus.CREATED) || order.getPayPalOrderId() == null) {
				continue;
			}
			Merchant merchant = merchantRepo.findByMerchantApiKey(order.getMerchantApiKey());

			try {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", new APIContext(cipher.decrypt(merchant.getClientId()),
						cipher.decrypt(merchant.getClientSecret()), "sandbox").fetchAccessToken());

				if (new Gson()
						.fromJson(restTemplate.exchange(
								properties.paypalOrdersCheckout + "/" + cipher.decrypt(order.getPayPalOrderId()),
								HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody(), JsonObject.class)
						.get("status").getAsString().equalsIgnoreCase("completed")) {
					log.info("Order: id=" + order.getId() + " status=COMPLETED");

					order.setStatus(OrderStatus.COMPLETED);
					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.SUCCESS)), Void.class);
					repo.save(order);
				}

			} catch (Exception e) {
				log.error("Order: id=" + order.getId() + " status=FAILED");

				order.setStatus(OrderStatus.FAILED);
				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.FAIL)), Void.class);
				repo.save(order);
			}
		}

	}

}
