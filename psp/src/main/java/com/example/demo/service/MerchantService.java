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
	private MerchantRepository merchantRepository;

	public List<Merchant> findAll() {
		log.info("MerchantService - findAll");
		return merchantRepository.findAll();
	}

	public Merchant findById(Integer id) throws NotFoundException {
		log.info("MerchantService - findById: id=" + id);
		Optional<Merchant> merchant = merchantRepository.findById(id);

		if (!merchant.isPresent()) {
			log.error("Merchant: id=" + id + " not found.");
			throw new NotFoundException(id.toString(), Merchant.class.getSimpleName());
		}

		return merchant.get();
	}

	public Merchant save(Merchant merchant) {
		merchant = merchantRepository.save(merchant);
		log.info("MerchantService - save: id=" + merchant.getId());
		return merchant;
	}

	public Merchant remove(Integer merchantId) throws NotFoundException {
		log.info("MerchantService - remove: id=" + merchantId);
		Optional<Merchant> merchant = merchantRepository.findById(merchantId);

		if (!merchant.isPresent()) {
			log.error("Merchant: id=" + merchantId + " not found.");
			throw new NotFoundException(merchantId.toString(), Merchant.class.getSimpleName());
		}

		merchant.get().setActive(false);
		return merchantRepository.save(merchant.get());
	}

	public Merchant findByApiKey(String apiKey) throws NotFoundException {
		log.info("MerchantService - findByApiKey: apiKey=" + apiKey);
		Optional<Merchant> merchant = merchantRepository.findByApiKey(apiKey);

		if (!merchant.isPresent()) {
			log.error("Merchant: apiKey=" + apiKey + " not found.");
			throw new NotFoundException(apiKey.toString(), Merchant.class.getSimpleName());
		}

		return merchant.get();
	}
}
