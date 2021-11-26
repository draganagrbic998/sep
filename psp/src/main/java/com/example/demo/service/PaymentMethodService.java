package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Merchant;
import com.example.demo.model.PaymentMethod;
import com.example.demo.repo.PaymentMethodRepository;

@Service
public class PaymentMethodService {

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private PaymentMethodRepository paymentMethodRepository;

	public List<PaymentMethod> findAll() {
		return paymentMethodRepository.findAll();
	}

	public PaymentMethod findById(Integer id) throws NotFoundException {
		Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);

		if (!paymentMethod.isPresent()) {
			throw new NotFoundException(id.toString(), PaymentMethod.class.getSimpleName());
		}

		return paymentMethod.get();
	}

	public PaymentMethod savePaymentMethod(PaymentMethod paymentMethod) {
		return paymentMethodRepository.save(paymentMethod);
	}

	public PaymentMethod removePaymentMethod(Integer paymentMethodId) throws NotFoundException {
		Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(paymentMethodId);

		if (!paymentMethod.isPresent()) {
			throw new NotFoundException(paymentMethodId.toString(), PaymentMethod.class.getSimpleName());
		}

		paymentMethod.get().setActive(false);
		return paymentMethodRepository.save(paymentMethod.get());
	}

	public List<PaymentMethod> getPaymentMethods(UUID appApiKey) throws NotFoundException {
		List<PaymentMethod> ret = new ArrayList<>();

		Merchant merchant = merchantService.findByApiKey(appApiKey.toString());

		for (PaymentMethod pm : merchant.getMethods()) {
			ret.add(pm);
		}

		return ret;
	}
}
