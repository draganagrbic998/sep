package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentCompletedDTO;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.repo.OrderRepository;
import com.example.demo.utils.DatabaseCipher;
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

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private OrderRepository repo;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private DatabaseCipher cipher;

	// Ovo ce biti metnuto u bazu u jednom trenutku
	// Svaki merchant ce ima svoje. Ovo nabavimo za testiranje na paypal-ov sajt.
	String clientId = "ASbalrTsNQwyeFRT6r47HW23NQwDpF9V_4IRJIEkhWGmgI2uZ5L7lYgrspWWgWvEYqd8GT1SmF4hcRd4";
	String clientSecret = "EJZT7rVvs4wBMCghAlPnx96WC-Se44lmQTKuiAXRWNFvFxH-e69d_aSI8gESJPAbbys3CvOmLZttfGPb";

	public Order findById(Long orderId) {
		log.info("OrderService - findById: id=" + orderId);
		return repo.getById(orderId);
	}

	public Order save(Order order) {
		order = repo.save(order);
		log.info("OrderService - save: id=" + order.getId());
		return order;
	}

	// Napravimo narudzbu kod paypal-a
	// Klijent prodavnice onda treba da potvrdi placanje
	public Order createPayment(Order order) {
		log.info("OrderService - createPayment: orderId=" + order.getId());

		Merchant merchant = cipher.decrypt(merchantService.findOneByApiKey(order.getMerchantApiKey()));

		Amount amount = new Amount();
		amount.setCurrency(order.getCurrency());
		amount.setTotal(order.getValue().toString());

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setReturnUrl("http://localhost:8086/view/success_url/" + order.getId());
		redirectUrls.setCancelUrl("http://localhost:8086/view/cancel_url/" + order.getId());
		payment.setRedirectUrls(redirectUrls);

		Payment createdPayment;
		try {
			log.info("createPayment - payment create using paypal api");
			APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");
			createdPayment = payment.create(context);
			if (createdPayment != null) {
				log.info("createPayment - payment created: payPalOrderId=" + createdPayment.getId());
				order.setStatus(OrderStatus.CREATED);
				order.setPayPalOrderId(cipher.encrypt(createdPayment.getId()));
				order = repo.save(order);
			}
		} catch (PayPalRESTException e) {
			log.error("createPayment - Error occured during payment creation");
		}
		return order;
	}

	// Poziva se nakon sto klijent odobri placanje
	public String completePayment(String paymentId, String payerId) {
		log.info("OrderService - completePayment: paymentId=" + paymentId + " payedId=" + payerId);

		log.info("completePayment - fetching order by payPalOrderId=" + paymentId);
		Order order = repo.findByPayPalOrderId(cipher.encrypt(paymentId)).get();
		Merchant merchant = cipher.decrypt(merchantService.findOneByApiKey(order.getMerchantApiKey()));

		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);

		Payment completedPayment = new Payment();

		try {
			log.info("completePayment - payment execution using paypal api");
			APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");
			completedPayment = payment.execute(context, paymentExecution);
			if (completedPayment != null) {
				order.setStatus(OrderStatus.COMPLETED);
			}
		} catch (PayPalRESTException e) {
			log.error("completePayment - Error occured during payment execution");
			System.err.println(e.getDetails());
			order.setStatus(OrderStatus.FAILED);
		}

		RestTemplate restTemplate = new RestTemplate();

		if (order.getStatus().toString().equalsIgnoreCase("COMPLETED")) {
			log.info("completePayment - payment execution status=COMPLETED");
			order.setExecuted(true);

			PaymentCompletedDTO paymentCompletedDTO = new PaymentCompletedDTO();
			paymentCompletedDTO.setId(order.getShopOrderId());
			paymentCompletedDTO.setStatus("COMPLETED");

			log.info("completePayment - notifying WebShop @" + order.getCallbackUrl());
			restTemplate.exchange(order.getCallbackUrl() + "/" + order.getShopOrderId(), HttpMethod.PUT,
					new HttpEntity<PaymentCompletedDTO>(paymentCompletedDTO), String.class);
		} else {
			log.error("completePayment - Error occured during payment execution");

			PaymentCompletedDTO paymentCompletedDTO = new PaymentCompletedDTO();
			paymentCompletedDTO.setId(order.getShopOrderId());
			paymentCompletedDTO.setStatus("FAILED");

			log.info("completePayment - notifying WebShop @" + order.getCallbackUrl());
			restTemplate.exchange(order.getCallbackUrl() + "/" + order.getShopOrderId(), HttpMethod.PUT,
					new HttpEntity<PaymentCompletedDTO>(paymentCompletedDTO), String.class);
		}

		repo.save(order);

		return completedPayment.toJSON();
	}

	public String getOrderDetails(Long orderId) throws PayPalRESTException {
		log.info("OrderService - getOrderDetails: orderId=" + orderId);

		Order order = repo.getById(orderId);
		Merchant merchant = cipher.decrypt(merchantService.findOneByApiKey(order.getMerchantApiKey()));

		APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");

		log.info("getOrderDetails - get payment details using paypal api");
		Payment payment = Payment.get(context, cipher.decrypt(order.getPayPalOrderId()));

		return payment.toJSON();
	}

	@Scheduled(fixedDelay = 300000)
	public void checkOrders() {
		log.info("OrderService - checkOrders");
		List<Order> orders = repo.findAllByExecuted(false);

		for (Order order : orders) {
			log.info("Order: id=" + order.getId() + " checking status");

			Merchant merchant = cipher.decrypt(merchantService.findOneByApiKey(order.getMerchantApiKey()));

			APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");

			try {
				String apiUrl = "https://api.sandbox.paypal.com/v2/checkout/orders/"
						+ cipher.decrypt(order.getPayPalOrderId());
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", "Bearer " + context.fetchAccessToken());
				HttpEntity entity = new HttpEntity(headers);

				log.info("checkOrders - get payment details using paypal api");
				ResponseEntity<String> details = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
				String status = (new Gson()).fromJson(details.getBody(), JsonObject.class).get("status").getAsString();

				if (status.equalsIgnoreCase("completed")) {
					log.info("Order: id=" + order.getId() + " status=COMPLETED");
					order.setStatus(OrderStatus.COMPLETED);
					order.setExecuted(true);

					PaymentCompletedDTO paymentCompletedDTO = new PaymentCompletedDTO();
					paymentCompletedDTO.setId(order.getShopOrderId());
					paymentCompletedDTO.setStatus("COMPLETED");

					log.info("checkOrders - notifying WebShop @" + order.getCallbackUrl());
					restTemplate.exchange(order.getCallbackUrl() + "/" + order.getShopOrderId(), HttpMethod.PUT,
							new HttpEntity<PaymentCompletedDTO>(paymentCompletedDTO), String.class);
				}

			} catch (Exception e) {
				log.error("checkOrders - Error occured while obtaining payment details");

				order.setStatus(OrderStatus.FAILED);
				order.setExecuted(true);

				PaymentCompletedDTO paymentCompletedDTO = new PaymentCompletedDTO();
				paymentCompletedDTO.setId(order.getShopOrderId());
				paymentCompletedDTO.setStatus("FAILED");

				log.info("checkOrders - notifying WebShop @" + order.getCallbackUrl());
				restTemplate.exchange(order.getCallbackUrl() + "/" + order.getShopOrderId(), HttpMethod.PUT,
						new HttpEntity<PaymentCompletedDTO>(paymentCompletedDTO), String.class);
			}
		}

		repo.saveAll(orders);
	}

}
