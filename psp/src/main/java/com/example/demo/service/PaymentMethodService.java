package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.PaymentMethod;
import com.example.demo.model.User;
import com.example.demo.repo.PaymentMethodRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@AllArgsConstructor
public class PaymentMethodService {

	private final PaymentMethodRepository repo;
	private final UserService userService;
	private final DiscoveryClient discoveryClient;

	@Transactional(readOnly = true)
	public List<PaymentMethod> read() {
		log.info("PaymentMethodService - findAll");
		return repo.findAll();
	}

	@Transactional(readOnly = true)
	public PaymentMethod readOne(Long id) {
		log.info("PaymentMethodService - readOne: id=" + id);
		Optional<PaymentMethod> paymentMethod = repo.findById(id);

		if (!paymentMethod.isPresent()) {
			log.error("PaymentMethod: id=" + id + " not found.");
			throw new NotFoundException(id.toString(), PaymentMethod.class.getSimpleName());
		}

		return paymentMethod.get();
	}

	@Transactional
	public PaymentMethod save(PaymentMethod paymentMethod) {
		List<String> available = getAllEurekaServices();
		if (!available.contains(paymentMethod.getName())) {
			throw new RuntimeException(); // za sad ovako
		}

		paymentMethod = repo.save(paymentMethod);
		log.info("PaymentMethodService - save: id=" + paymentMethod.getId());
		return paymentMethod;
	}

	@Transactional
	public void delete(Long id) {
		log.info("PaymentMethodService - delete: id=" + id);
		Optional<PaymentMethod> paymentMethod = repo.findById(id);

		if (!paymentMethod.isPresent()) {
			log.error("PaymentMethod: id=" + id + " not found.");
			throw new NotFoundException(id.toString(), PaymentMethod.class.getSimpleName());
		}

		repo.deleteById(id);
	}

	public List<PaymentMethod> getPaymentMethods(UUID merchantApiKey) {
		log.info("PaymentMethodService - getPaymentMethods: merchantApiKey=" + merchantApiKey.toString());
		List<PaymentMethod> ret = new ArrayList<>();

		User merchant = userService.findByApiKey(merchantApiKey.toString());

		for (PaymentMethod pm : merchant.getMethods())
			ret.add(pm);

		return ret;
	}

	private List<String> getAllEurekaServices() {
		log.info("PaymentMethodService - getAllEurekaServices");
		List<String> services = discoveryClient.getServices();
		for (String temp : services) {
			List<ServiceInstance> list = discoveryClient.getInstances(temp);
			for (ServiceInstance si : list) {
				System.out.println(temp);
				System.out.println(si.getUri());
				System.out.println(si.getHost());
				System.out.println(si.getPort());
				System.out.println("=====================");
			}
		}
		services.remove("psp");
		return services;
	}

}
