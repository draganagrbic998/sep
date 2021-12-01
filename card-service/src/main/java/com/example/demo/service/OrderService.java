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

	public Order save(Order order) {
		return repo.save(order);
	}

	public List<Order> findAll() {
		return repo.findAll();
	}

	public Order findById(Integer id) throws NotFoundException {
		Optional<Order> order = repo.findById(id);

		if (!order.isPresent()) {
			throw new NotFoundException(id.toString(), Order.class.getSimpleName());
		}

		return order.get();
	}

	public String pay(Integer orderId, String merchantApiKey) throws NotFoundException {
		Order order = this.findById(orderId);
		Merchant merchant = merchantService.findByMerchantApiKey(merchantApiKey);

		PaymentRequestDTO dto = paymentRequestMapper.toDTO(merchant, order);

		ResponseEntity<PaymentRequestResponseDTO> responseEntity = restTemplate.exchange(merchant.getBankUrl(),
				HttpMethod.POST, new HttpEntity<PaymentRequestDTO>(dto), PaymentRequestResponseDTO.class);

		this.save(order);

		return responseEntity.getBody().getPaymentUrl() + "/" + responseEntity.getBody().getPaymentId();
	}

	public String completePayment(PaymentRequestCompletedDTO paymentRequestCompletedDTO) throws NotFoundException {
		if (paymentRequestCompletedDTO.getStatus().contentEquals("SUCCESS")) {
			Order order = this.findById(paymentRequestCompletedDTO.getId());

			order.setOrderStatus(OrderStatus.COMPLETED);
			order = this.save(order);

			paymentRequestCompletedDTO.setId(order.getShopOrderId());
			paymentRequestCompletedDTO.setStatus("COMPLETED");

			ResponseEntity<String> responseEntity = restTemplate.exchange(order.getCallbackUrl(), HttpMethod.POST,
					new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO), String.class);

			return responseEntity.getBody();

		} else if (paymentRequestCompletedDTO.getStatus().contentEquals("FAILED")) {
			Order order = this.findById(paymentRequestCompletedDTO.getId());

			order.setOrderStatus(OrderStatus.FAILED);
			order = this.save(order);

			paymentRequestCompletedDTO.setId(order.getShopOrderId());

			ResponseEntity<String> responseEntity = restTemplate.exchange(order.getCallbackUrl(), HttpMethod.POST,
					new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO), String.class);

			return responseEntity.getBody();
		} else {
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
		List<Order> orders = this.findAll();

		for (Order order : orders) {
			Merchant merchant = merchantService.findByMerchantApiKeyOptional(order.getMerchantApiKey()).get();

			if (order.getOrderStatus() == OrderStatus.CREATED) {
				if (order.getTicks() < 5) {
					order.setTicks(order.getTicks() + 1);
					this.save(order);

					try {
						ResponseEntity<String> responseEntity = restTemplate
								.getForEntity(merchant.getBankUrl() + "/" + order.getShopOrderId(), String.class);
						if (responseEntity.getBody() == "SUCCESSFUL") {
							order.setOrderStatus(OrderStatus.COMPLETED);
							this.save(order);

							PaymentRequestCompletedDTO paymentRequestCompletedDTO = new PaymentRequestCompletedDTO();
							paymentRequestCompletedDTO.setId(order.getShopOrderId());
							paymentRequestCompletedDTO.setStatus("COMPLETED");

							new RestTemplate().exchange(order.getCallbackUrl(), HttpMethod.POST,
									new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO),
									String.class);
						}
					} catch (Exception e) {
						// ovde log
						System.out.println("Nepostojeca transakcija");
					}

				} else {
					order.setOrderStatus(OrderStatus.FAILED);
					this.save(order);

					PaymentRequestCompletedDTO paymentRequestCompletedDTO = new PaymentRequestCompletedDTO();
					paymentRequestCompletedDTO.setId(order.getShopOrderId());
					paymentRequestCompletedDTO.setStatus("FAILED");

					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.POST,
							new HttpEntity<PaymentRequestCompletedDTO>(paymentRequestCompletedDTO), String.class);

				}
			}
		}
	}

}
