package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.CoingateOrderInfoDTO;
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

	public Order findOne(Long orderId) {
		log.info("OrderService - findOne: id=" + orderId);
		return repo.getById(orderId);
	}

	public Order save(Order order) {
		log.info("OrderService - save: id=" + order.getId());
		return repo.save(cipher.encrypt(order));
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
		coingateOrder.setCallback_url(properties.completeUrl + "/" + order.getId());

		coingateOrder.setToken(UUID.randomUUID().toString());

		String coingateToken = "Bearer " + merchant.getCoingateToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", coingateToken);

		log.info("createPayment - creating order using coingate api");

		ResponseEntity<CoingateOrder> responseEntity = new RestTemplate().exchange(properties.bitcoinOrders,
				HttpMethod.POST, new HttpEntity<CoingateOrder>(coingateOrder, headers), CoingateOrder.class);

		order.setCoingateOrderId(responseEntity.getBody().getId().toString());
		order.setPaymentUrl(responseEntity.getBody().getPayment_url());

		merchantRepo.save(cipher.encrypt(merchant));
		return repo.save(cipher.encrypt(order));
	}

	@Scheduled(fixedDelay = 300000)
	public void checkOrders() {
		log.info("OrderService - checkOrders");
		List<Order> orders = repo.findAll();

		for (Order order : orders) {
			order = cipher.decrypt(order);
			if (order.getCoingateOrderId() == null) {
				continue;
			}

			if (order.getStatus() == OrderStatus.CREATED || order.getStatus() == OrderStatus.EXPIRED) {
				Merchant merchant = cipher
						.decrypt(merchantRepo.findByMerchantApiKey(cipher.encrypt(order.getMerchantApiKey())));

				String coingateToken = "Bearer " + merchant.getCoingateToken();
				HttpHeaders headers = new HttpHeaders();
				headers.add("Authorization", coingateToken);
				HttpEntity entity = new HttpEntity(headers);

				ResponseEntity<CoingateOrderInfoDTO> response = restTemplate.exchange(
						properties.bitcoinOrders + "/" + order.getCoingateOrderId(), HttpMethod.GET, entity,
						CoingateOrderInfoDTO.class);
				CoingateOrderInfoDTO responseBody = response.getBody();

				if (responseBody.getStatus().contentEquals("paid")) {
					order.setStatus(OrderStatus.COMPLETED);

					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.SUCCESS)),
							Void.class);
				} else if (responseBody.getStatus().contentEquals("expired")) {
					order.setStatus(OrderStatus.FAILED);

					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.FAIL)),
							Void.class);
				} else if (responseBody.getStatus().contentEquals("invalid")) {
					order.setStatus(OrderStatus.FAILED);

					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.FAIL)),
							Void.class);
				}

				merchantRepo.save(cipher.encrypt(merchant));
				repo.save(cipher.encrypt(order));

			}
		}

	}

	public String completePayment(Long orderId) {
		log.info("OrderService - completePayment: orderId=" + orderId);
		Order order = cipher.decrypt(repo.getById(orderId));
		Merchant merchant = cipher
				.decrypt(merchantRepo.findByMerchantApiKey(cipher.encrypt(order.getMerchantApiKey())));

		String coingateToken = "Bearer " + merchant.getCoingateToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", coingateToken);
		HttpEntity entity = new HttpEntity(headers);

		ResponseEntity<CoingateOrderInfoDTO> response = restTemplate.exchange(
				properties.bitcoinOrders + "/" + order.getCoingateOrderId(), HttpMethod.GET, entity,
				CoingateOrderInfoDTO.class);
		CoingateOrderInfoDTO responseBody = response.getBody();

		if (responseBody.getStatus().contentEquals("paid")) {
			order.setStatus(OrderStatus.COMPLETED);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.SUCCESS)), Void.class);
		} else if (responseBody.getStatus().contentEquals("expired")) {
			order.setStatus(OrderStatus.FAILED);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.FAIL)), Void.class);
		} else if (responseBody.getStatus().contentEquals("invalid")) {
			order.setStatus(OrderStatus.FAILED);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.FAIL)), Void.class);
		}

		merchantRepo.save(cipher.encrypt(merchant));
		return repo.save(cipher.encrypt(order)).getStatus().toString();
	}

}
