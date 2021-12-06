package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentRequestCompleted;
import com.example.demo.dto.PaymentRequestResponse;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.repo.OrderRepository;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class OrderService {

	private final RestTemplate restTemplate;
	private final OrderRepository repo;
	private final MerchantService merchantService;
	private final DatabaseCipher cipher;

	public List<Order> read() {
		log.info("OrderService - read");
		return repo.findAll();
	}

	public Order readOne(Long id) {
		log.info("OrderService - readOne: id=" + id);
		Optional<Order> order = repo.findById(id);

		if (!order.isPresent()) {
			log.error("Order: id=" + id + " not found.");
			throw new NotFoundException(id.toString(), Order.class.getSimpleName());
		}

		return order.get();
	}

	public Order save(Order order) {
		log.info("OrderService - save: id=" + order.getId());
		return repo.save(cipher.encrypt(order));
	}

	public String pay(Long orderId, String merchantApiKey) {
		log.info("OrderService - pay: orderId=" + orderId + " merchantApiKey=" + merchantApiKey);
		Order order = readOne(orderId);
		Merchant merchant = cipher.decrypt(merchantService.findByMerchantApiKey(merchantApiKey));
		PaymentRequest dto = new PaymentRequest(merchant, order);

		log.info("pay - create PaymentRequest in bank @" + merchant.getBankUrl());
		PaymentRequestResponse temp = restTemplate.exchange(merchant.getBankUrl() + "/payment-requests",
				HttpMethod.POST, new HttpEntity<PaymentRequest>(dto), PaymentRequestResponse.class).getBody();
		save(order);

		log.info("pay - obtained: paymentUrl=" + temp.getPaymentUrl() + " paymentId=" + temp.getPaymentId());
		return temp.getPaymentUrl() + "/" + temp.getPaymentId();
	}

	public String complete(PaymentRequestCompleted requestCompleted) {
		log.info("OrderService - completePayment: orderId=" + requestCompleted.getId());
		Order order = readOne(requestCompleted.getId());

		if (requestCompleted.getStatus().equals(OrderStatus.SUCCESS)) { // treba completed??
			log.info("Order: id=" + order.getId() + " payment_status=SUCCESS");
			order.setOrderStatus(OrderStatus.COMPLETED);
			order = save(order);

			log.info("completePayment - notifying WebShop @" + order.getCallbackUrl());
			return restTemplate.exchange(order.getCallbackUrl() + "/" + order.getOrderIdWebShop(), HttpMethod.PUT,
					new HttpEntity<PaymentRequestCompleted>(
							new PaymentRequestCompleted(order.getOrderIdWebShop(), OrderStatus.COMPLETED)),
					String.class).getBody();

		} else if (requestCompleted.getStatus().equals(OrderStatus.FAILED)) {
			log.info("Order: id=" + order.getId() + " payment_status=FAILED");
			order.setOrderStatus(OrderStatus.FAILED);
			order = save(order);

			log.info("completePayment - notifying WebShop @" + order.getCallbackUrl());
			return restTemplate.exchange(order.getCallbackUrl() + "/" + order.getOrderIdWebShop(), HttpMethod.PUT,
					new HttpEntity<PaymentRequestCompleted>(
							new PaymentRequestCompleted(order.getOrderIdWebShop(), OrderStatus.FAILED)),
					String.class).getBody();
		} else {
			log.info("Order: id=" + order.getId() + " - Error occured while paying for the order");
			return "Error occured while paying for the order";
		}
	}

	// Kao kod paypal-a
	// Ako ovaj servis pukne ocemo da proverimo da li je u medjuvremenu
	// potvrdjeno placanje u banci i to evidentiramo gde treba
	// Takodje ako vidimo da posle 5 minuta u banci ne prolazi placanje
	// to znaci da ili kupac nije potvrdio ili je pukla banka
	@Scheduled(fixedDelay = 60000)
	public void checkOrders() {
		log.info("OrderService - checkOrders");

		for (Order order : read()) {
			Merchant merchant = cipher
					.decrypt(merchantService.findByMerchantApiKeyOptional(order.getMerchantApiKey()).get());

			if (order.getOrderStatus().equals(OrderStatus.CREATED)) {
				if (order.getTicks() < 5) {
					log.info("Order: id=" + order.getId() + " tick=" + order.getTicks() + " - OK");
					order.setTicks(order.getTicks() + 1);
					save(order);

					try {
						log.info("checkOrders - get order state from bank @" + merchant.getBankUrl());
						String status = restTemplate
								.getForEntity(merchant.getBankUrl() + "/" + order.getOrderIdWebShop(), String.class)
								.getBody();

						if (status.equals("SUCCESSFUL")) {
							log.info("Order: id=" + order.getId() + " payment_status=SUCCESSFUL");
							order.setOrderStatus(OrderStatus.COMPLETED);
							save(order);

							log.info("checkOrders - notifying WebShop @" + order.getCallbackUrl());
							restTemplate.exchange(order.getCallbackUrl() + "/" + order.getOrderIdWebShop(),
									HttpMethod.PUT, new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(
											order.getOrderIdWebShop(), OrderStatus.COMPLETED)),
									String.class);
						}
					} catch (Exception e) {
						log.info("Order: id=" + order.getId() + " - Order doesn't exist");
					}

				} else {
					log.warn("Order: id=" + order.getId() + " tick=" + order.getTicks() + " - FAILED");
					order.setOrderStatus(OrderStatus.FAILED);
					save(order);

					log.info("checkOrders - notifying WebShop @" + order.getCallbackUrl());
					restTemplate.exchange(order.getCallbackUrl() + "/" + order.getOrderIdWebShop(), HttpMethod.PUT,
							new HttpEntity<PaymentRequestCompleted>(
									new PaymentRequestCompleted(order.getOrderIdWebShop(), OrderStatus.FAILED)),
							String.class);

				}
			}
		}
	}

}
