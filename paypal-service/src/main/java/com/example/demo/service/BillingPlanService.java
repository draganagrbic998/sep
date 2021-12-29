package com.example.demo.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentCompletedDTO;
import com.example.demo.model.BillingPlan;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.PaymentStatus;
import com.example.demo.model.Product;
import com.example.demo.repository.BillingPlanRepository;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
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
public class BillingPlanService {

	private final BillingPlanRepository repo;
	private final MerchantRepository merchantRepo;
	private final OrderRepository orderRepo;
	private final ProductRepository productRepo;
	private final DatabaseCipher cipher;
	private final RestTemplate restTemplate;
	private final PropertiesData properties;

	public BillingPlan create(Long orderId, Long duration) {
		log.info("BillingPlanService - createBillingPlan: orderId=" + orderId);

		Order order = orderRepo.findById(orderId).get();
		Merchant merchant = merchantRepo.findByMerchantApiKey(order.getMerchantApiKey());
		Product product = createProduct(order, merchant);
		BillingPlan billingPlan = new BillingPlan();

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", new APIContext(cipher.decrypt(merchant.getClientId()),
					cipher.decrypt(merchant.getClientSecret()), "sandbox").fetchAccessToken());

			Gson gson = new Gson();
			String res = restTemplate.postForObject(properties.paypalPlans,
					new HttpEntity<>("{\n" + "  \"name\": \"Subscription - " + cipher.decrypt(product.getName())
							+ "\",\n" + "  \"product_id\": \"" + cipher.decrypt(product.getProductId()) + "\",\n"
							+ "  \"billing_cycles\": [{\n" + "    \"frequency\": {\n"
							+ "      \"interval_unit\": \"MONTH\",\n" + "      \"interval_count\": 1\n" + "    },\n"
							+ "    \"pricing_scheme\": {\n" + "	    \"fixed_price\": {\n" + "    	  \"value\": \""
							+ order.getPrice() + "\",\n" + "     	  \"currency_code\": \"" + order.getCurrency()
							+ "\"\n" + "    	}\n" + "    },\n" + "	\"tenure_type\": \"REGULAR\",\n"
							+ "	\"sequence\": 1,\n" + "	\"total_cycles\": " + duration + "\n" + "  }],\n"
							+ "  \"payment_preferences\": {\n" + "    \"payment_failure_threshold\": 3,\n"
							+ "    \"auto_bill_outstanding\": true\n" + "  }\n" + "}", headers),
					String.class);

			billingPlan.setPlanId(gson.fromJson(res, JsonObject.class).get("id").getAsString());
			billingPlan.setProductId(cipher.decrypt(product.getProductId()));
			billingPlan.setName("Subscription - " + cipher.decrypt(product.getName()));
			billingPlan.setStatus(gson.fromJson(res, JsonObject.class).get("status").getAsString());
		} catch (Exception e) {
			log.error("createBillingPlan - Error occured while creating billing plan");

			order.setStatus(OrderStatus.FAILED);
			order.setExecuted(true);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<>(new PaymentCompletedDTO(PaymentStatus.ERROR)), Void.class);
			orderRepo.save(order);
		}

		return repo.save(cipher.encrypt(billingPlan));
	}

	private Product createProduct(Order order, Merchant merchant) {
		log.info("BillingPlanService - createProduct");
		Product product = new Product();

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", new APIContext(cipher.decrypt(merchant.getClientId()),
					cipher.decrypt(merchant.getClientSecret()), "sandbox").fetchAccessToken());

			Gson gson = new Gson();
			String res = restTemplate.postForObject(properties.paypalProducts,
					new HttpEntity<>("{\n" + "  \"name\": \"Product " + order.getId() + "\",\n"
							+ "  \"type\": \"SERVICE\",\n" + "  \"category\": \"SOFTWARE\"\n" + "  }]\n" + "}",
							headers),
					String.class);

			product.setProductId(gson.fromJson(res, JsonObject.class).get("id").getAsString());
			product.setName(gson.fromJson(res, JsonObject.class).get("name").getAsString());
		} catch (Exception e) {
			log.error("createProduct - Error occured while creating paypal product");

			order.setStatus(OrderStatus.FAILED);
			order.setExecuted(true);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<>(new PaymentCompletedDTO(PaymentStatus.ERROR)), Void.class);
			orderRepo.save(order);
		}

		return productRepo.save(cipher.encrypt(product));
	}

}
