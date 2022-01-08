package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.OrderStatusUpdate;
import com.example.demo.dto.SubscriptionDTO;
import com.example.demo.model.BillingPlan;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.PaymentStatus;
import com.example.demo.model.Subscription;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.utils.DatabaseCipher;
import com.example.demo.utils.PropertiesData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class SubscriptionService {

	private final SubscriptionRepository repo;
	private final MerchantRepository merchantRepo;
	private final OrderRepository orderRepo;
	private final BillingPlanService planService;
	private final DatabaseCipher cipher;
	private final RestTemplate restTemplate;
	private final PropertiesData properties;

	public String getDetails(Long id) throws PayPalRESTException {
		log.info("SubscriptionService - getDetails: id=" + id);
		Subscription subscription = repo.findById(id).get();
		Merchant merchant = merchantRepo
				.findByMerchantApiKey(orderRepo.getById(subscription.getOrderId()).getMerchantApiKey());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", new APIContext(cipher.decrypt(merchant.getClientId()),
				cipher.decrypt(merchant.getClientSecret()), "sandbox").fetchAccessToken());

		return restTemplate
				.exchange(properties.paypalSubscriptions + "/" + cipher.decrypt(subscription.getSubscriptionId()),
						HttpMethod.GET, new HttpEntity<>(headers), String.class)
				.getBody();
	}

	public Subscription create(Long orderId, SubscriptionDTO dto) {
		log.info("SubscriptionService - create: orderId=" + orderId);
		BillingPlan plan = planService.create(orderId, dto.getDuration());
		Order order = orderRepo.findById(orderId).get();
		Merchant merchant = merchantRepo.findByMerchantApiKey(order.getMerchantApiKey());
		Subscription subscription = new Subscription();

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", new APIContext(cipher.decrypt(merchant.getClientId()),
					cipher.decrypt(merchant.getClientSecret()), "sandbox").fetchAccessToken());

			Gson gson = new Gson();
			String res = restTemplate.exchange(properties.paypalSubscriptions, HttpMethod.POST,
					new HttpEntity<>("{\n" + "  \"plan_id\": \"" + cipher.decrypt(plan.getPlanId()) + "\",\n"
							+ "  \"start_time\": \""
							+ new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
									.format(new Date(new Date().getTime() + 600000))
							+ "\",\n" + "  \"subscirber\": {\n" + "    \"email_address\": \"" + dto.getEmail() + "\"\n"
							+ "  }" + "}", headers),
					String.class).getBody();

			subscription.setStatus(gson.fromJson(res, JsonObject.class).get("status").getAsString());
			subscription.setOrderId(order.getId());
			subscription.setPlanId(cipher.decrypt(plan.getPlanId()));
			subscription.setSubscriptionId(gson.fromJson(res, JsonObject.class).get("id").getAsString());

			subscription.setSubscriber(dto.getEmail());
			subscription.setDuration(dto.getDuration());
			subscription.setPrice(order.getPrice());
			subscription.setCurrency(order.getCurrency());

			subscription.setCallbackUrl(order.getCallbackUrl());
			subscription.setApproveUrl(gson.fromJson(res, JsonObject.class).get("links").getAsJsonArray().get(0)
					.getAsJsonObject().get("href").getAsString());

		} catch (Exception e) {
			log.error("create - Error occured while creating paypal subscription");

			subscription.setStatus("ERROR");
			order.setStatus(OrderStatus.FAILED);
			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.ERROR)), Void.class);
			orderRepo.save(order);

		}

		return repo.save(cipher.encrypt(subscription));
	}

	public String complete(String subscriptionId, Long subscriptionDBId) {
		log.info("SubscriptionService - complete: subscriptionId=" + subscriptionId + ", subscriptionDBId="
				+ subscriptionDBId);
		Subscription subscription = repo.findById(subscriptionDBId).get();
		Order order = orderRepo.getById(subscription.getOrderId());

		try {
			if (new Gson().fromJson(getDetails(subscriptionDBId), JsonObject.class).get("status")
					.getAsString() != null) {

				subscription.setStatus("ACTIVE");
				order.setStatus(OrderStatus.COMPLETED);
				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.SUCCESS)), Void.class);

			} else {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			log.error("complete - Error occured while completing paypal subscription");

			subscription.setStatus("ERROR");
			order.setStatus(OrderStatus.FAILED);
			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.ERROR)), Void.class);

		}

		orderRepo.save(order);
		return repo.save(subscription).getStatus();
	}

	@Scheduled(fixedDelay = 300000)
	public void checkSubscriptions() {
		log.info("SubscriptionService - checkSubscriptions");

		for (Subscription subscription : repo.findAll()) {
			if (List.of("active", "failed", "error").contains(subscription.getStatus().toLowerCase())) {
				continue;
			}
			Order order = orderRepo.getById(subscription.getOrderId());

			try {
				if (new Gson().fromJson(getDetails(subscription.getId()), JsonObject.class).get("status").getAsString()
						.equalsIgnoreCase("active")) {
					log.info("Subscription: id=" + subscription.getId() + " status=ACTIVE");

					subscription.setStatus("ACTIVE");
					order.setStatus(OrderStatus.COMPLETED);
					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.SUCCESS)), Void.class);

					orderRepo.save(order);
					repo.save(subscription);

				}
			} catch (Exception e) {
				log.info("Subscription: id=" + subscription.getId() + " status=ERROR");

				subscription.setStatus("ERROR");
				order.setStatus(OrderStatus.FAILED);
				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<>(new OrderStatusUpdate(PaymentStatus.ERROR)), Void.class);

				orderRepo.save(order);
				repo.save(subscription);
			}

		}

	}

}
