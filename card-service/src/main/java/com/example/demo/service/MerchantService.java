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
	private MerchantRepository repo;

	public List<Merchant> findAll() {
		return repo.findAll();
	}

	public Merchant findOne(Integer id) {
		return repo.getById(id);
	}

	public Optional<Merchant> findByMerchantApiKeyOptional(String merchantApiKey) {
		return repo.findByMerchantApiKey(merchantApiKey);
	}

	public Merchant findByMerchantApiKey(String merchantApiKey) throws NotFoundException {
		Optional<Merchant> merchant = repo.findByMerchantApiKey(merchantApiKey);

		if (!merchant.isPresent()) {
			throw new NotFoundException(merchantApiKey, Merchant.class.getSimpleName());
		}

		return merchant.get();
	}

	public Merchant save(Merchant merchant) {
		return repo.save(merchant);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}

}
