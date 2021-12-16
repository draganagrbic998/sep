package com.example.demo.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentCompletedDTO;
import com.example.demo.dto.SubscriptionDetailsDTO;
import com.example.demo.model.BillingPlan;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.PaymentStatus;
import com.example.demo.model.Subscription;
import com.example.demo.repo.OrderRepository;
import com.example.demo.repo.SubscriptionRepository;
import com.example.demo.utils.DatabaseCipher;
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

	private final RestTemplate restTemplate;

	private final DatabaseCipher cipher;

	private final SubscriptionRepository subscriptionRepository;

	private final OrderRepository orderRepository;

	private final MerchantService merchantService;

	public Subscription findById(Long subscriptionId) {
		log.info("SubscriptionService - findById: id=" + subscriptionId);
		return subscriptionRepository.getById(subscriptionId);
	}

	public Optional<Subscription> getSubscriptionByPlanId(String planId) {
		log.info("SubscriptionService - getSubscriptionByPlanId: planId=" + planId);
		return subscriptionRepository.findByPlanId(cipher.encrypt(planId));
	}

	public Subscription createSubscription(Order order, BillingPlan plan, SubscriptionDetailsDTO subscriptionDTO) {
		log.info("SubscriptionService - createSubscription: orderId=" + order.getId() + " planId=" + plan.getPlanId());

		Optional<Subscription> subscriptionOptional = subscriptionRepository.findByPlanId(plan.getPlanId());

		if (subscriptionOptional.isPresent()) {
			log.warn("createSubscription - subrscription already created: planId=" + plan.getPlanId());
			return subscriptionOptional.get();
		}

		plan = cipher.decrypt(plan);

		Merchant merchant = cipher.decrypt(merchantService.findOneByApiKey(order.getMerchantApiKey()));
		APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

		// Mora da se doda neko vreme na trenutni datum i vreme
		// da bi PayPal prihvatio pretplatu...
		Date startTime = new Date(new Date().getTime() + 600000);

		String paypalAPI = "https://api.sandbox.paypal.com/v1/billing/subscriptions";
		String json = "{\n" + "  \"plan_id\": \"" + plan.getPlanId() + "\",\n" + "  \"start_time\": \""
				+ df.format(startTime) + "\",\n" + "  \"subscirber\": {\n" + "    \"email_address\": \""
				+ subscriptionDTO.getEmail() + "\"\n" + "  }" + "}";

		Subscription subscription = new Subscription();

		try {
			log.info("createSubscription - create subscription using paypal api");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", context.fetchAccessToken());
			HttpEntity<String> entity = new HttpEntity<String>(json, headers);

			String res = restTemplate.postForObject(paypalAPI, entity, String.class);

			Gson gson = new Gson();

			subscription.setSubscriptionId(gson.fromJson(res, JsonObject.class).get("id").getAsString());
			subscription.setPlanId(plan.getPlanId());
			subscription.setApproveUrl(gson.fromJson(res, JsonObject.class).get("links").getAsJsonArray().get(0)
					.getAsJsonObject().get("href").getAsString());
			subscription.setStatus(gson.fromJson(res, JsonObject.class).get("status").getAsString());
			subscription.setOrderId(order.getId());
			subscription.setPrice(order.getPrice());
			subscription.setCurrency(order.getCurrency());
			subscription.setCallbackUrl(order.getCallbackUrl());
			subscription.setDuration(subscriptionDTO.getDuration());
			subscription.setSubscriber(subscriptionDTO.getEmail());
		} catch (Exception e) {
			log.error("createSubscription - Error occured while creating paypal subscription");
			e.printStackTrace();

			order.setStatus(OrderStatus.FAILED);
			order.setExecuted(true);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.ERROR)), Void.class);

			orderRepository.save(order);
		}

		plan = cipher.encrypt(plan);
		merchant = cipher.encrypt(merchant);
		return subscriptionRepository.save(cipher.encrypt(subscription));
	}

	public String getSubscriptionDetails(Long subscriptionId) throws PayPalRESTException {
		log.info("SubscriptionService - getSubscriptionDetails: subscriptionId=" + subscriptionId);

		Subscription subscription = cipher.decrypt(subscriptionRepository.getById(subscriptionId));

		Order order = orderRepository.getById(subscription.getOrderId());
		Merchant merchant = cipher.decrypt(merchantService.findOneByApiKey(order.getMerchantApiKey()));
		APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");

		String api = "https://api.sandbox.paypal.com/v1/billing/subscriptions/" + subscription.getSubscriptionId();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", context.fetchAccessToken());
		HttpEntity entity = new HttpEntity(headers);

		subscription = cipher.encrypt(subscription);
		merchant = cipher.encrypt(merchant);

		log.info("getSubscriptionDetails - get subscription details using paypal api");
		return restTemplate.exchange(api, HttpMethod.GET, entity, String.class).getBody();
	}

	public String getSubscriptionDetails(Long subscriptionId, String subscriptionIdPpl) throws PayPalRESTException {
		log.info("SubscriptionService - getSubscriptionDetails: subscriptionId=" + subscriptionId
				+ " subscriptionIdPpl=" + subscriptionIdPpl);

		Subscription subscription = cipher.decrypt(subscriptionRepository.getById(subscriptionId));

		Order order = orderRepository.getById(subscription.getOrderId());
		Merchant merchant = cipher.decrypt(merchantService.findOneByApiKey(order.getMerchantApiKey()));
		APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");

		String api = "https://api.sandbox.paypal.com/v1/billing/subscriptions/" + subscriptionIdPpl;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", context.fetchAccessToken());
		HttpEntity entity = new HttpEntity(headers);

		subscription = cipher.encrypt(subscription);
		merchant = cipher.encrypt(merchant);

		log.info("getSubscriptionDetails - get subscription details using paypal api");
		return restTemplate.exchange(api, HttpMethod.GET, entity, String.class).getBody();
	}

	public String completeSubscription(String subscriptionId, Long subscriptionDBId) {
		log.info("SubscriptionService - activateSubscription: subscriptionId=" + subscriptionId + " subscriptionDBId="
				+ subscriptionDBId);

		log.info("activateSubscription - fetching subscription by id=" + subscriptionDBId);
		Subscription subscription = subscriptionRepository.getById(subscriptionDBId);

		log.info("activateSubscription - fetching order by orderId=" + subscription.getOrderId());
		Order order = orderRepository.getById(subscription.getOrderId());

		try {
			String res = getSubscriptionDetails(subscriptionDBId, subscriptionId);
			Gson gson = new Gson();
			String status = gson.fromJson(res, JsonObject.class).get("status").getAsString();

			if (status.equalsIgnoreCase("ACTIVE")) {
				log.info("activateSubscription - subscription activation: status=ACTIVE");
				subscription.setStatus("ACTIVE");

				order.setStatus(OrderStatus.COMPLETED);
				order.setExecuted(true);

				log.info("activateSubscription - notifying WebShop @" + order.getCallbackUrl());
				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.SUCCESS)),
						Void.class);
			} else {
				log.error("activateSubscription - Error occured during subscription activation");
				subscription.setStatus("FAILED");

				order.setStatus(OrderStatus.FAILED);
				order.setExecuted(true);

				log.info("activateSubscription - notifying WebShop @" + order.getCallbackUrl());
				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.FAIL)), Void.class);
			}
		} catch (Exception e) {
			log.error("activateSubscription - Error occured during subscription details retrieval");
			subscription.setStatus("ERROR");
			e.printStackTrace();
			order.setStatus(OrderStatus.FAILED);
			order.setExecuted(true);

			log.info("completePayment - notifying WebShop @" + order.getCallbackUrl());
			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.ERROR)), Void.class);
		}

		orderRepository.save(order);
		return subscriptionRepository.save(subscription).getStatus();
	}

	@Scheduled(fixedDelay = 300000)
	public void checkSubscriptions() {
		log.info("SubscriptionService - checkSubscriptions");
		List<Subscription> subscriptions = subscriptionRepository.findAllByStatus("APPROVAL_PENDING");

		for (Subscription subscription : subscriptions) {
			log.info("Subscription: id=" + subscription.getId() + " checking status");
			Order order = orderRepository.getById(subscription.getOrderId());

			try {
				String res = getSubscriptionDetails(subscription.getId());
				Gson gson = new Gson();
				String status = gson.fromJson(res, JsonObject.class).get("status").getAsString();

				if (status.equalsIgnoreCase("ACTIVE")) {
					log.info("activateSubscription - subscription activation: status=ACTIVE");
					subscription.setStatus("ACTIVE");

					order.setStatus(OrderStatus.COMPLETED);
					order.setExecuted(true);

					log.info("activateSubscription - notifying WebShop @" + order.getCallbackUrl());
					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.SUCCESS)),
							Void.class);
				} else {
					log.error("activateSubscription - Error occured during subscription activation");
					subscription.setStatus("FAILED");

					order.setStatus(OrderStatus.FAILED);
					order.setExecuted(true);

					log.info("activateSubscription - notifying WebShop @" + order.getCallbackUrl());
					restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
							new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.FAIL)),
							Void.class);
				}
			} catch (Exception e) {
				log.error("activateSubscription - Error occured during subscription details retrieval");
				subscription.setStatus("ERROR");
				e.printStackTrace();
				order.setStatus(OrderStatus.FAILED);
				order.setExecuted(true);

				log.info("completePayment - notifying WebShop @" + order.getCallbackUrl());
				restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
						new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.ERROR)), Void.class);
			}
		}

		subscriptionRepository.saveAll(subscriptions);
	}

}
