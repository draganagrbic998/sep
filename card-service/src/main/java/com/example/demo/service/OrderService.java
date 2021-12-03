package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.example.exception.NotFoundException;
import com.example.demo.dto.PaymentRequestCompletedDTO;
import com.example.demo.dto.PaymentRequestDTO;
import com.example.demo.dto.PaymentRequestResponseDTO;
import com.example.demo.mapper.PaymentRequestMapper;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.repo.OrderRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class OrderService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private OrderRepository repo;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private PaymentRequestMapper paymentRequestMapper;

	public List<Order> findAll() {
		log.info("OrderService - findAll");
		return repo.findAll();
	}

	public Order findById(Integer id) throws NotFoundException {
		log.info("OrderService - findById: id=" + id);
		Optional<Order> order = repo.findById(id);

		if (!order.isPresent()) {
			log.error("Order: id=" + id + " not found.");
			throw new NotFoundException(id.toString(), Order.class.getSimpleName());
		}

		return order.get();
	}

	public Order save(Order order) {
		order = repo.save(order);
		log.info("OrderService - save: id=" + order.getId());
		return order;
	}

	public String pay(Integer orderId, String merchantApiKey) throws NotFoundException {
		log.info("OrderService - pay: orderId=" + orderId + " merchantApiKey=" + merchantApiKey);
		Order order = this.findById(orderId);
		Merchant merchant = merchantService.findByMerchantApiKey(merchantApiKey);

		PaymentRequestDTO dto = paymentRequestMapper.toDTO(merchant, order);

		log.info("pay - create PaymentRequest in bank @" + merchant.getBankUrl());
		ResponseEntity<PaymentRequestResponseDTO> responseEntity = restTemplate.exchange(
				merchant.getBankUrl() + "/payment-requests", HttpMethod.POST, new HttpEntity<PaymentRequestDTO>(dto),
				PaymentRequestResponseDTO.class);

		this.save(order);

		log.info("pay - obtained: paymentUrl=" + responseEntity.getBody().getPaymentUrl() + " paymentId="
				+ responseEntity.getBody().getPaymentId());
		return responseEntity.getBody().getPaymentUrl() + "/" + responseEntity.getBody().getPaymentId();
	}

	public String completePayment(PaymentRequestCompletedDTO paymentRequestCompletedDTO) throws NotFoundException {
		log.info("OrderService - completePayment: orderId=" + paymentRequestCompletedDTO.getId());
		Order order = this.findById(paymentRequestCompletedDTO.getId());

		if (paymentRequestCompletedDTO.getStatus().contentEquals("SUCCESS")) {
			log.info("Order: id=" + order.getId() + " payment_status=SUCCESS");

			order.setOrderStatus(OrderStatus.COMPLETED);
			order = this.save(order);

			paymentRequestCompletedDTO.setId(order.getShopOrderId());
			paymentRequestCompletedDTO.setStatus("COMPLETED");

			log.info("completePayment - notifying WebShop @" + order.getCallbackUrl());
			ResponseEntity<String> responseEntity = restTemplate.exchange(order.getCallbackUrl(), HttpMethod.POST,
					new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO), String.class);

			return responseEntity.getBody();

		} else if (paymentRequestCompletedDTO.getStatus().contentEquals("FAILED")) {
			log.info("Order: id=" + order.getId() + " payment_status=FAILED");

			order.setOrderStatus(OrderStatus.FAILED);
			order = this.save(order);

			paymentRequestCompletedDTO.setId(order.getShopOrderId());

			log.info("completePayment - notifying WebShop @" + order.getCallbackUrl());
			ResponseEntity<String> responseEntity = restTemplate.exchange(order.getCallbackUrl(), HttpMethod.POST,
					new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO), String.class);

			return responseEntity.getBody();
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
		List<Order> orders = this.findAll();

		for (Order order : orders) {
			Merchant merchant = merchantService.findByMerchantApiKeyOptional(order.getMerchantApiKey()).get();

			if (order.getOrderStatus() == OrderStatus.CREATED) {
				if (order.getTicks() < 5) {
					log.info("Order: id=" + order.getId() + " tick=" + order.getTicks() + " - OK");
					order.setTicks(order.getTicks() + 1);
					this.save(order);

					try {
						log.info("checkOrders - get order state from bank @" + merchant.getBankUrl());
						ResponseEntity<String> responseEntity = restTemplate
								.getForEntity(merchant.getBankUrl() + "/" + order.getShopOrderId(), String.class);

						if (responseEntity.getBody() == "SUCCESSFUL") {
							log.info("Order: id=" + order.getId() + " payment_status=SUCCESSFUL");
							order.setOrderStatus(OrderStatus.COMPLETED);
							this.save(order);

							PaymentRequestCompletedDTO paymentRequestCompletedDTO = new PaymentRequestCompletedDTO();
							paymentRequestCompletedDTO.setId(order.getShopOrderId());
							paymentRequestCompletedDTO.setStatus("COMPLETED");

							log.info("checkOrders - notifying WebShop @" + order.getCallbackUrl());
							new RestTemplate().exchange(order.getCallbackUrl(), HttpMethod.POST,
									new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO),
									String.class);
						}
					} catch (Exception e) {
						log.info("Order: id=" + order.getId() + " - Order doesn't exist");
					}

				} else {
					log.warn("Order: id=" + order.getId() + " tick=" + order.getTicks() + " - FAILED");
					order.setOrderStatus(OrderStatus.FAILED);
					this.save(order);

					PaymentRequestCompletedDTO paymentRequestCompletedDTO = new PaymentRequestCompletedDTO();
					paymentRequestCompletedDTO.setId(order.getShopOrderId());
					paymentRequestCompletedDTO.setStatus("FAILED");

					log.info("checkOrders - notifying WebShop @" + order.getCallbackUrl());
					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.POST,
							new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO), String.class);

				}
			}
		}
	}

}
