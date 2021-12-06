package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentRequestCompleted;
import com.example.demo.dto.PaymentRequestResponse;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.PaymentStatus;
import com.example.demo.repo.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class OrderService {

	private final RestTemplate restTemplate;
	private final OrderRepository repo;
	private final MerchantService merchantService;

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
		return repo.save(order);
	}

	public String pay(Long orderId) {
		log.info("OrderService - pay: orderId=" + orderId);
		Order order = readOne(orderId);
		Merchant merchant = merchantService.findByMerchantApiKey(order.getMerchantApiKey());
		// nema enkripcije
		PaymentRequest dto = new PaymentRequest(merchant, order);

		log.info("pay - create PaymentRequest in bank @" + merchant.getBankUrl());
		PaymentRequestResponse response = restTemplate.exchange(merchant.getBankUrl() + "/payment-requests",
				HttpMethod.POST, new HttpEntity<PaymentRequest>(dto), PaymentRequestResponse.class).getBody();
		// save(order);

		log.info("pay - obtained: paymentUrl=" + response.getUrl() + " paymentId=" + response.getUrl());
		return response.getUrl() + "/" + response.getId();
	}

	public String complete(Long orderId, PaymentRequestCompleted requestCompleted) {
		log.info("OrderService - completePayment: orderId=" + orderId);
		Order order = readOne(orderId);

		if (requestCompleted.getStatus().equals(PaymentStatus.SUCCESS)) { // treba completed??
			log.info("Order: id=" + order.getId() + " payment_status=SUCCESS");
			order.setStatus(OrderStatus.COMPLETED);
			order = save(order);

			log.info("completePayment - notifying WebShop @" + order.getCallbackUrl());
			return restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.SUCCESS)),
					String.class).getBody();

		} else if (requestCompleted.getStatus().equals(PaymentStatus.FAIL)) {
			log.info("Order: id=" + order.getId() + " payment_status=FAILED");
			order.setStatus(OrderStatus.FAILED);
			order = save(order);

			log.info("completePayment - notifying WebShop @" + order.getCallbackUrl());
			return restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.FAIL)),
					String.class).getBody();
		} else {
			log.info("Order: id=" + order.getId() + " - Error occured while paying for the order");
			return "Error occured while paying for the order";
		}
	}

	@Scheduled(fixedDelay = 60000)
	public void checkOrders() {
		log.info("OrderService - checkOrders");

		for (Order order : read()) {
			Merchant merchant = merchantService.findByMerchantApiKeyOptional(order.getMerchantApiKey()).get();

			if (order.getStatus().equals(OrderStatus.CREATED)) {
				if (order.getTicks() < 5) {
					log.info("Order: id=" + order.getId() + " tick=" + order.getTicks() + " - OK");
					order.setTicks(order.getTicks() + 1);
					save(order);

					try {
						log.info("checkOrders - get order state from bank @" + merchant.getBankUrl());
						String status = restTemplate.getForEntity(merchant.getBankUrl(), String.class).getBody();

						if (status.equals("SUCCESSFUL")) {
							log.info("Order: id=" + order.getId() + " payment_status=SUCCESSFUL");
							order.setStatus(OrderStatus.COMPLETED);
							save(order);

							log.info("checkOrders - notifying WebShop @" + order.getCallbackUrl());
							restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
									new HttpEntity<PaymentRequestCompleted>(
											new PaymentRequestCompleted(PaymentStatus.SUCCESS)),
									String.class);
						}
					} catch (Exception e) {
						log.info("Order: id=" + order.getId() + " - Order doesn't exist");
					}

				} else {
					log.warn("Order: id=" + order.getId() + " tick=" + order.getTicks() + " - FAILED");
					order.setStatus(OrderStatus.FAILED);
					save(order);

					log.info("checkOrders - notifying WebShop @" + order.getCallbackUrl());
					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.FAIL)),
							String.class);

				}
			}
		}
	}

}
