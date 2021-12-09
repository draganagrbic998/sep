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

	public String pay(Long id) {
		log.info("OrderService - pay: id=" + id);
		Order order = repo.findById(id).get();
		Merchant merchant = merchantRepo.findByMerchantApiKey(order.getMerchantApiKey());

		log.info("OrderService - create PaymentRequest in bank @" + merchant.getBankUrl());
		PaymentRequestResponse response = restTemplate
				.exchange(merchant.getBankUrl() + "/payment-requests", HttpMethod.POST,
						new HttpEntity<PaymentRequest>(
								new PaymentRequest(merchant, order, properties.completeUrl, properties.viewUrl)),
						PaymentRequestResponse.class)
				.getBody();
		return response.getUrl() + "/" + response.getId() + "?qr=true";
	}

	public Order complete(Long id, PaymentStatus status) {
		log.info("OrderService - complete: id=" + id);
		Order order = repo.findById(id).get();

		if (status.equals(PaymentStatus.SUCCESS)) {
			log.info("Order: id=" + order.getId() + " payment_status=SUCCESS");
			order.setStatus(OrderStatus.COMPLETED);
			order = save(order);

			log.info("OrderService - complete: notifying WebShop @" + order.getCallbackUrl());
			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.SUCCESS)),
					Void.class);
			return order;

		} else {
			log.info("Order: id=" + order.getId() + " payment_status=FAILED");
			order.setStatus(OrderStatus.FAILED);
			order = save(order);

			log.info("OrderService - complete: notifying WebShop @" + order.getCallbackUrl());
			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.FAIL)),
					Void.class);
			return order;
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

					log.info("OrderService - checkOrders: notifying WebShop @" + order.getCallbackUrl());
					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<PaymentRequestCompleted>(new PaymentRequestCompleted(PaymentStatus.FAIL)),
							Void.class);

				}
			}
		}
	}

}
