package com.example.demo.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentRequestCompleted;
import com.example.demo.dto.PaymentRequestResponse;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.PaymentStatus;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class OrderService {

	private final OrderRepository repo;
	private final MerchantRepository merchantRepo;
	private final RestTemplate restTemplate;
	private final PropertiesData properties;

	public Order save(Order order) {
		log.info("OrderService - save: id=" + order.getId());
		return repo.save(order);
	}

	public String pay(Long orderId) {
		log.info("OrderService - pay: orderId=" + orderId);
		Order order = repo.findById(orderId).get();
		Merchant merchant = merchantRepo.findByMerchantApiKey(order.getMerchantApiKey());
		PaymentRequest request = new PaymentRequest(merchant, order, properties.completeUrl, properties.viewUrl);

		log.info("OrderService - create PaymentRequest in bank @" + merchant.getBankUrl());
		PaymentRequestResponse response = restTemplate.exchange(merchant.getBankUrl() + "/payment-requests",
				HttpMethod.POST, new HttpEntity<PaymentRequest>(request), PaymentRequestResponse.class).getBody();
		log.info("OrderService - obtained: paymentUrl=" + response.getUrl() + " paymentId=" + response.getUrl());
		return response.getUrl() + "/" + response.getId();
	}

	public String complete(Long orderId, PaymentRequestCompleted completed) {
		log.info("OrderService - complete: orderId=" + orderId);
		Order order = repo.findById(orderId).get();

		if (completed.getStatus().equals(PaymentStatus.SUCCESS)) {
			log.info("Order: id=" + order.getId() + " payment_status=SUCCESS");
			order.setStatus(OrderStatus.COMPLETED);
			order = save(order);

			log.info("OrderService - complete: notifying WebShop @" + order.getCallbackUrl());
			return restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.SUCCESS)),
					String.class).getBody();

		} else {
			log.info("Order: id=" + order.getId() + " payment_status=FAILED");
			order.setStatus(OrderStatus.FAILED);
			order = save(order);

			log.info("OrderService - complete: notifying WebShop @" + order.getCallbackUrl());
			return restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.FAIL)),
					String.class).getBody();
		}
	}

	@Scheduled(fixedDelay = 60000)
	public void checkOrders() {
		log.info("OrderService - checkOrders");

		for (Order order : repo.findAll()) {
			if (order.getStatus().equals(OrderStatus.CREATED)) {
				if (order.getTicks() < 5) {
					log.info("Order: id=" + order.getId() + " tick=" + order.getTicks() + " - OK");
					order.setTicks(order.getTicks() + 1);
					save(order);

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
