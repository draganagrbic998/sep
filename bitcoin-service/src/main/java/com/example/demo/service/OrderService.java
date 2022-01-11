package com.example.demo.service;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.CoingateOrderInfoDTO;
import com.example.demo.dto.OrderStatusUpdate;
import com.example.demo.model.CoingateOrder;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.PaymentStatus;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.utils.DatabaseCipher;
import com.example.demo.utils.PropertiesData;

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

	public Order findOne(Long id) {
		log.info("OrderService - findOne: id=" + id);
		return repo.findById(id).get();
	}

	public Order save(Order order) {
		log.info("OrderService - save: id=" + order.getId());
		return repo.save(cipher.encrypt(order));
	}

	public Order createPayment(Long id) {
		log.info("OrderService - createPayment: id=" + id);
		Order order = repo.findById(id).get();
		Merchant merchant = merchantRepo.findByMerchantApiKey(order.getMerchantApiKey());

		CoingateOrder coingateOrder = new CoingateOrder();
		coingateOrder.setOrder_id(order.getId().toString());
		coingateOrder.setPrice_amount(order.getPrice());
		coingateOrder.setPrice_currency(order.getCurrency());

		coingateOrder.setToken(UUID.randomUUID().toString());
		coingateOrder.setSuccess_url(properties.successUrl);
		coingateOrder.setCancel_url(properties.cancelUrl);
		coingateOrder.setCallback_url(properties.completeUrl + "/" + order.getId());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + cipher.decrypt(merchant.getCoingateToken()));
		CoingateOrder response = restTemplate.exchange(properties.bitcoinOrdersUrl, HttpMethod.POST,
				new HttpEntity<>(coingateOrder, headers), CoingateOrder.class).getBody();

		order.setCoingateOrderId(cipher.encrypt(response.getId().toString()));
		order.setPaymentUrl(response.getPayment_url());
		return repo.save(order);
	}

	public String completePayment(Long id) {
		log.info("OrderService - completePayment: id=" + id);
		Order order = repo.findById(id).get();
		Merchant merchant = merchantRepo.findByMerchantApiKey(order.getMerchantApiKey());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + cipher.decrypt(merchant.getCoingateToken()));
		CoingateOrderInfoDTO response = restTemplate
				.exchange(properties.bitcoinOrdersUrl + "/" + cipher.decrypt(order.getCoingateOrderId()),
						HttpMethod.GET, new HttpEntity<>(headers), CoingateOrderInfoDTO.class)
				.getBody();

		if (response.getStatus().contentEquals("paid")) {
			order.setStatus(OrderStatus.COMPLETED);
			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.SUCCESS)), Void.class);
		} else if (response.getStatus().contentEquals("expired")) {
			order.setStatus(OrderStatus.FAILED);
			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.FAIL)), Void.class);
		} else if (response.getStatus().contentEquals("invalid")) {
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
			if (order.getCoingateOrderId() == null) {
				continue;
			}
			if (!order.getStatus().equals(OrderStatus.CREATED) && !order.getStatus().equals(OrderStatus.EXPIRED)) {
				continue;
			}

			Merchant merchant = merchantRepo.findByMerchantApiKey(order.getMerchantApiKey());
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + cipher.decrypt(merchant.getCoingateToken()));

			CoingateOrderInfoDTO response = restTemplate
					.exchange(properties.bitcoinOrdersUrl + "/" + cipher.decrypt(order.getCoingateOrderId()),
							HttpMethod.GET, new HttpEntity<>(headers), CoingateOrderInfoDTO.class)
					.getBody();

			if (response.getStatus().contentEquals("paid")) {
				order.setStatus(OrderStatus.COMPLETED);
				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.SUCCESS)), Void.class);
			} else if (response.getStatus().contentEquals("expired")) {
				order.setStatus(OrderStatus.FAILED);
				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.FAIL)), Void.class);
			} else if (response.getStatus().contentEquals("invalid")) {
				order.setStatus(OrderStatus.FAILED);
				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.FAIL)), Void.class);
			}

			repo.save(order);
		}

	}

}
