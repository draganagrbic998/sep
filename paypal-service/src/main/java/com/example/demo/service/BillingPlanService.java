package com.example.demo.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentCompletedDTO;
import com.example.demo.model.BillingPlan;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.PaymentStatus;
import com.example.demo.model.Product;
import com.example.demo.repo.BillingPlanRepository;
import com.example.demo.repo.OrderRepository;
import com.example.demo.repo.ProductRepository;
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
public class BillingPlanService {

	private final RestTemplate restTemplate;

	private final ProductRepository productRepository;

	private final BillingPlanRepository planRepository;

	private final DatabaseCipher cipher;

	private final MerchantService merchantService;

	private final OrderRepository orderRepository;

	public BillingPlan readOne(Long id) {
		log.info("BillingPlanService - readOne: id=" + id);
		return planRepository.getById(id);
	}

	public Product createProduct(Order order) {
		log.info("BillingPlanService - createProduct");

		Merchant merchant = cipher.decrypt(merchantService.findOneByApiKey(order.getMerchantApiKey()));
		APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");

		String api = "https://api.sandbox.paypal.com/v1/catalogs/products";
		String json = "{\n" + "  \"name\": \"Product " + order.getId() + "\",\n" + "  \"type\": \"SERVICE\",\n"
				+ "  \"category\": \"SOFTWARE\"\n" + "  }]\n" + "}";

		Product product = new Product();

		try {
			log.info("createProduct - create product using paypal api");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", context.fetchAccessToken());
			HttpEntity<String> entity = new HttpEntity<String>(json, headers);

			String res = restTemplate.postForObject(api, entity, String.class);

			Gson gson = new Gson();

			product.setProductId(gson.fromJson(res, JsonObject.class).get("id").getAsString());
			product.setName(gson.fromJson(res, JsonObject.class).get("name").getAsString());
		} catch (Exception e) {
			log.error("createProduct - Error occured while creating paypal product");

			order.setStatus(OrderStatus.FAILED);
			order.setExecuted(true);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.ERROR)), Void.class);

			orderRepository.save(order);
		}

		merchant = cipher.encrypt(merchant);
		return productRepository.save(cipher.encrypt(product));
	}

	public BillingPlan createBillingPlan(Order order, Product product, Long duration) {
		log.info("BillingPlanService - createBillingPlan: productId=" + product.getProductId());

		product = cipher.decrypt(product);
		Merchant merchant = cipher.decrypt(merchantService.findOneByApiKey(order.getMerchantApiKey()));
		APIContext context = new APIContext(merchant.getClientId(), merchant.getClientSecret(), "sandbox");

		String api = "https://api.sandbox.paypal.com/v1/billing/plans";
		String json = "{\n" + "  \"name\": \"Subscription - " + product.getName() + "\",\n" + "  \"product_id\": \""
				+ product.getProductId() + "\",\n" + "  \"billing_cycles\": [{\n" + "    \"frequency\": {\n"
				+ "      \"interval_unit\": \"MONTH\",\n" + "      \"interval_count\": 1\n" + "    },\n"
				+ "    \"pricing_scheme\": {\n" + "	    \"fixed_price\": {\n" + "    	  \"value\": \""
				+ order.getPrice() + "\",\n" + "     	  \"currency_code\": \"" + order.getCurrency() + "\"\n"
				+ "    	}\n" + "    },\n" + "	\"tenure_type\": \"REGULAR\",\n" + "	\"sequence\": 1,\n"
				+ "	\"total_cycles\": " + duration + "\n" + "  }],\n" + "  \"payment_preferences\": {\n"
				+ "    \"payment_failure_threshold\": 3,\n" + "    \"auto_bill_outstanding\": true\n" + "  }\n" + "}";

		BillingPlan billingPlan = new BillingPlan();

		try {
			log.info("createBillingPlan - create billing plan using paypal api");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", context.fetchAccessToken());
			HttpEntity<String> entity = new HttpEntity<String>(json, headers);

			String res = restTemplate.postForObject(api, entity, String.class);

			Gson gson = new Gson();

			billingPlan.setPlanId(gson.fromJson(res, JsonObject.class).get("id").getAsString());
			billingPlan.setProductId(product.getProductId());
			billingPlan.setName("Subscription - " + product.getName());
			billingPlan.setStatus(gson.fromJson(res, JsonObject.class).get("status").getAsString());
		} catch (Exception e) {
			log.error("createBillingPlan - Error occured while creating billing plan");

			order.setStatus(OrderStatus.FAILED);
			order.setExecuted(true);

			restTemplate.exchange(order.getCallbackUrl(), HttpMethod.PUT,
					new HttpEntity<PaymentCompletedDTO>(new PaymentCompletedDTO(PaymentStatus.ERROR)), Void.class);

			orderRepository.save(order);
		}

		product = cipher.encrypt(product);
		merchant = cipher.encrypt(merchant);
		return planRepository.save(cipher.encrypt(billingPlan));
	}

}
