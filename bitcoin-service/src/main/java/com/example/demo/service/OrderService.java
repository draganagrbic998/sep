package com.example.demo.service;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentCompletedDTO;
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

			return "";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Order createPayment(Long id) {
		log.info("OrderService - createPayment: id=" + id);
		Order order = cipher.decrypt(repo.findById(id).get());
		Merchant merchant = cipher
				.decrypt(merchantRepo.findByMerchantApiKey(cipher.encrypt(order.getMerchantApiKey())));

		CoingateOrder coingateOrder = new CoingateOrder();
		coingateOrder.setOrder_id(order.getId().toString());
		coingateOrder.setPrice_amount(order.getPrice());
		coingateOrder.setPrice_currency(order.getCurrency());

		coingateOrder.setCancel_url(properties.cancelUrl);
		coingateOrder.setSuccess_url(properties.successUrl);
		coingateOrder.setCallback_url(order.getCallbackUrl());

		coingateOrder.setToken(UUID.randomUUID().toString());

		String coingateToken = "Bearer " + merchant.getCoingateToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", coingateToken);

		log.info("createPayment - creating order using coingate api");

		ResponseEntity<CoingateOrder> responseEntity = new RestTemplate().exchange(properties.bitcoinOrders,
				HttpMethod.POST, new HttpEntity<CoingateOrder>(coingateOrder, headers), CoingateOrder.class);

		order.setCoingateOrderId(responseEntity.getBody().getId().toString());

		merchantRepo.save(cipher.encrypt(merchant));
		return repo.save(cipher.encrypt(order));
	}

	public String completePayment(String paymentId, String payerId) {
		log.info("OrderService - completePayment: paymentId=" + paymentId + ", payerId=" + payerId);
		Order order = cipher.decrypt(repo.findByPayPalOrderId(cipher.encrypt(paymentId)).get());
		Merchant merchant = cipher
				.decrypt(merchantRepo.findByMerchantApiKey(cipher.encrypt(order.getMerchantApiKey())));

		merchantRepo.save(cipher.encrypt(merchant));
		repo.save(cipher.encrypt(order));
		return "";
	}

	@Scheduled(fixedDelay = 300000)
	public void checkOrders() {
		log.info("OrderService - checkOrders");

		for (Order order : repo.findAllByExecuted(false)) {
			order = cipher.decrypt(order);
			if (order.getCoingateOrderId() == null) {
				continue;
			}

			Merchant merchant = cipher
					.decrypt(merchantRepo.findByMerchantApiKey(cipher.encrypt(order.getMerchantApiKey())));

			try {

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
