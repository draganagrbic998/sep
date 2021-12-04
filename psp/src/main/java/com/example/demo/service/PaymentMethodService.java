package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Merchant;
import com.example.demo.model.PaymentMethod;
import com.example.demo.repo.PaymentMethodRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@AllArgsConstructor
public class PaymentMethodService {

	private final MerchantService merchantService;
	private final PaymentMethodRepository paymentMethodRepository;
	private final DiscoveryClient discoveryClient;

	public List<PaymentMethod> findAll() {
		log.info("PaymentMethodService - findAll");
		return paymentMethodRepository.findAll();
	}

	public PaymentMethod findById(Integer id) throws NotFoundException {
		log.info("PaymentMethodService - findById: id=" + id);
		Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);

		if (!paymentMethod.isPresent()) {
			log.error("PaymentMethod: id=" + id + " not found.");
			throw new NotFoundException(id.toString(), PaymentMethod.class.getSimpleName());
		}

		return paymentMethod.get();
	}

	public PaymentMethod save(PaymentMethod paymentMethod) {
		paymentMethod = paymentMethodRepository.save(paymentMethod);
		log.info("PaymentMethodService - save: id=" + paymentMethod.getId());
		return paymentMethod;
	}

	public void remove(Integer paymentMethodId) throws NotFoundException {
		log.info("PaymentMethodService - remove: id=" + paymentMethodId);
		Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(paymentMethodId);

		if (!paymentMethod.isPresent()) {
			log.error("PaymentMethod: id=" + paymentMethodId + " not found.");
			throw new NotFoundException(paymentMethodId.toString(), PaymentMethod.class.getSimpleName());
		}

		paymentMethodRepository.deleteById(paymentMethodId);
	}

	public List<PaymentMethod> getPaymentMethods(UUID merchantApiKey) throws NotFoundException {
		log.info("PaymentMethodService - getPaymentMethods: merchantApiKey=" + merchantApiKey.toString());
		List<PaymentMethod> ret = new ArrayList<>();

		Merchant merchant = merchantService.findByApiKey(merchantApiKey.toString());

		for (PaymentMethod pm : merchant.getMethods())
			ret.add(pm);

		return ret;
	}

	// Da bi prilikom dodavanja nacina placanja proverili da li uopste imamo
	// takav servis na eureci, jer dzaba mi dodajemo ako to nema da radi
	public List<String> getAllEurekaServices() {
		log.info("PaymentMethodService - getAllEurekaServices");
		List<String> services = discoveryClient.getServices();
		services.remove("psp");
		return services;
	}

}
