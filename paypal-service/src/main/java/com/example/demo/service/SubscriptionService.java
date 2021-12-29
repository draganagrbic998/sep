package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentCompletedDTO;
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

	public Subscription create(Long orderId, SubscriptionDTO dto) {
		log.info("SubscriptionService - create: orderId=" + orderId);
		BillingPlan plan = planService.create(orderId, dto.getDuration());

		Optional<Subscription> subscriptionOptional = repo.findByPlanId(plan.getPlanId());
		if (subscriptionOptional.isPresent()) {
			log.warn("create - subrscription already created: planId=" + cipher.decrypt(plan.getPlanId()));
			return cipher.decrypt(subscriptionOptional.get()); // is this fine?
		}

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

			subscription.setSubscriptionId(gson.fromJson(res, JsonObject.class).get("id").getAsString());
			subscription.setOrderId(order.getId());
			subscription.setPlanId(cipher.decrypt(plan.getPlanId()));

			subscription.setStatus(gson.fromJson(res, JsonObject.class).get("status").getAsString());
			subscription.setSubscriber(dto.getEmail());
			subscription.setDuration(dto.getDuration());

			subscription.setPrice(order.getPrice());
			subscription.setCurrency(order.getCurrency());

			subscription.setCallbackUrl(order.getCallbackUrl());
			subscription.setApproveUrl(gson.fromJson(res, JsonObject.class).get("links").getAsJsonArray().get(0)
					.getAsJsonObject().get("href").getAsString());
		} catch (Exception e) {
			log.error("create - Error occured while creating paypal subscription");

			order.setStatus(OrderStatus.FAILED);
			order.setExecuted(true);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<>(new PaymentCompletedDTO(PaymentStatus.ERROR)), Void.class);
			orderRepo.save(order);
		}

		return repo.save(cipher.encrypt(subscription));
	}

	public String getDetails(Long subscriptionDBId) {
		try {
			log.info("SubscriptionService - getDetails: subscriptionDBId=" + subscriptionDBId);

			Subscription subscription = repo.findById(subscriptionDBId).get();
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String complete(String subscriptionId, Long subscriptionDBId) {
		log.info("SubscriptionService - complete: subscriptionId=" + subscriptionId + ", subscriptionDBId="
				+ subscriptionDBId);

		Subscription subscription = repo.findById(subscriptionDBId).get();
		Order order = orderRepo.getById(subscription.getOrderId());

		try {
			if (new Gson().fromJson(getDetails(subscriptionDBId), JsonObject.class).get("status").getAsString()
					.equalsIgnoreCase("ACTIVE")) {
				log.info("activateSubscription - subscription activated");

				subscription.setStatus("ACTIVE");
				order.setStatus(OrderStatus.COMPLETED);
				order.setExecuted(true);

				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<>(new PaymentCompletedDTO(PaymentStatus.SUCCESS)),
						Void.class);
			} else {
				log.info("activateSubscription - subscription activated");

				subscription.setStatus("ACTIVE");
				order.setStatus(OrderStatus.COMPLETED);
				order.setExecuted(true);

				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<>(new PaymentCompletedDTO(PaymentStatus.SUCCESS)),
						Void.class);
			}
		} catch (Exception e) {
			log.error("activateSubscription - Error occured during subscription details retrieval");

			subscription.setStatus("ERROR");
			order.setStatus(OrderStatus.FAILED);
			order.setExecuted(true);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<>(new PaymentCompletedDTO(PaymentStatus.ERROR)), Void.class);
		}

		orderRepo.save(order);
		return repo.save(subscription).getStatus();
	}

	@Scheduled(fixedDelay = 300000)
	public void checkSubscriptions() {
		log.info("SubscriptionService - checkSubscriptions");

		for (Subscription subscription : repo.findAllByStatus("APPROVAL_PENDING")) {
			Order order = orderRepo.getById(subscription.getOrderId());

			try {
				if (new Gson().fromJson(getDetails(subscription.getId()), JsonObject.class).get("status").getAsString()
						.equalsIgnoreCase("ACTIVE")) {
					log.info("Subscription: id=" + subscription.getId() + " status=ACTIVE");

					subscription.setStatus("ACTIVE");
					order.setStatus(OrderStatus.COMPLETED);
					order.setExecuted(true);

					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<>(new PaymentCompletedDTO(PaymentStatus.SUCCESS)),
							Void.class);
				} else {
					log.error("Subscription: id=" + subscription.getId() + " status=FAILED");

					subscription.setStatus("FAILED");
					order.setStatus(OrderStatus.FAILED);
					order.setExecuted(true);

					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<>(new PaymentCompletedDTO(PaymentStatus.FAIL)),
							Void.class);
				}
			} catch (Exception e) {
				log.error("checkSubscriptions - Error occured during subscription details retrieval");

				subscription.setStatus("ERROR");
				order.setStatus(OrderStatus.FAILED);
				order.setExecuted(true);

				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<>(new PaymentCompletedDTO(PaymentStatus.ERROR)), Void.class);
			}

			orderRepo.save(order);
			repo.save(subscription);
		}

	}

}
