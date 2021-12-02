package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.Merchant;
import com.example.demo.repo.MerchantRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MerchantService {

	@Autowired
	private MerchantRepository repo;

	public List<Merchant> findAll() {
		log.info("MerchantService - findAll");
		return repo.findAll();
	}

	public Merchant findOne(Integer id) {
		log.info("MerchantService - findOne: id=" + id);
		return repo.getById(id);
	}

	public Optional<Merchant> findByMerchantApiKeyOptional(String merchantApiKey) {
		log.info("MerchantService - findByMerchantApiKeyOptional: merchantApiKey=" + merchantApiKey);
		return repo.findByMerchantApiKey(merchantApiKey);
	}

	public Merchant findByMerchantApiKey(String merchantApiKey) throws NotFoundException {
		log.info("MerchantService - findByMerchantApiKey: apiKey=" + merchantApiKey);
		Optional<Merchant> merchant = repo.findByMerchantApiKey(merchantApiKey);

		if (!merchant.isPresent()) {
			log.error("Merchant: merchantApiKey=" + merchantApiKey + " not found.");
			throw new NotFoundException(merchantApiKey, Merchant.class.getSimpleName());
		}

		return merchant.get();
	}

	public Merchant save(Merchant merchant) {
		merchant = repo.save(merchant);
		log.info("MerchantService - save: id=" + merchant.getId());
		return merchant;
	}

	public void delete(Integer id) {
		log.info("MerchantService - delete: id=" + id);
		repo.deleteById(id);
	}

}
