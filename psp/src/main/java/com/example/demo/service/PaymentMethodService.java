package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.PaymentMethod;
import com.example.demo.model.User;
import com.example.demo.repo.PaymentMethodRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class PaymentMethodService {

	private final PaymentMethodRepository repo;
	private final UserService userService;
	private final DiscoveryClient discoveryClient;

	@Transactional(readOnly = true)
	public List<PaymentMethod> read() {
		log.info("PaymentMethodService - read");
		return repo.findAll();
	}

	@Transactional
	public PaymentMethod save(PaymentMethod paymentMethod) {
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

	public List<String> toAdd() {
		log.info("PaymentMethodService - getAllEurekaServices");
		return discoveryClient.getServices().stream()
				.filter(item -> !item.equals("psp") && !item.startsWith("webshop")
						&& !read().stream().map(pm -> pm.getName()).collect(Collectors.toList()).contains(item))
				.collect(Collectors.toList());
	}

	public List<PaymentMethod> getPaymentMethods(UUID merchantApiKey) {
		log.info("PaymentMethodService - getPaymentMethods: merchantApiKey=" + merchantApiKey.toString());
		List<PaymentMethod> ret = new ArrayList<>();

		User merchant = userService.findByApiKey(merchantApiKey.toString());

		for (PaymentMethod pm : merchant.getMethods())
			ret.add(pm);

		return ret;
	}

}
