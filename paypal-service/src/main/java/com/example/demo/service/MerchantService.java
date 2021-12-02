package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public Merchant findOneByApiKey(String apiKey) {
		log.info("MerchantService - findOneByApiKey: apiKey=" + apiKey);
		return repo.findByMerchantApiKey(apiKey);
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
