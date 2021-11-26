package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Merchant;
import com.example.demo.repo.MerchantRepository;

@Service
public class MerchantService {

	@Autowired
	private MerchantRepository merchantRepository;

	public List<Merchant> findAll() {
		return merchantRepository.findAll();
	}

	public Merchant findById(Integer id) throws NotFoundException {
		Optional<Merchant> merchant = merchantRepository.findById(id);

		if (!merchant.isPresent()) {
			throw new NotFoundException(id.toString(), Merchant.class.getSimpleName());
		}

		return merchant.get();
	}

	public Merchant saveMerchant(Merchant merchant) {
		return merchantRepository.save(merchant);
	}

	public Merchant removeMerchant(Integer merchantId) throws NotFoundException {
		Optional<Merchant> merchant = merchantRepository.findById(merchantId);

		if (!merchant.isPresent()) {
			throw new NotFoundException(merchantId.toString(), Merchant.class.getSimpleName());
		}

		merchant.get().setActive(false);
		return merchantRepository.save(merchant.get());
	}

	public Merchant findByApiKey(String apiKey) throws NotFoundException {
		Optional<Merchant> merchant = merchantRepository.findByApiKey(apiKey);

		if (!merchant.isPresent()) {
			throw new NotFoundException(apiKey.toString(), Merchant.class.getSimpleName());
		}

		return merchant.get();
	}
}
