package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Merchant;
import com.example.demo.repo.MerchantRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class MerchantService {

	private final MerchantRepository repo;

	public List<Merchant> read() {
		log.info("MerchantService - read");
		return repo.findAll();
	}

	public Merchant readOne(Long id) {
		log.info("MerchantService - readOne: id=" + id);
		return repo.getById(id);
	}

	public Merchant findOneByApiKey(String apiKey) {
		log.info("MerchantService - findOneByApiKey: apiKey=" + apiKey);
		return repo.findByMerchantApiKey(apiKey);
	}

	public Merchant save(Merchant merchant) {
		merchant = repo.save(merchant);
		log.info("MerchantService - save: id=" + merchant.getId());
		return merchant;
	}

	public void delete(Long id) {
		log.info("MerchantService - delete: id=" + id);
		repo.deleteById(id);
	}

}
