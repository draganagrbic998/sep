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
		log.info("MerchantService - findOne: id=" + id.toString());
		return repo.getById(id);
	}

	public Merchant findOneByApiKey(String apiKey) {
		log.info("MerchantService - findOneByApiKey: apiKey=" + apiKey);
		return repo.findByMerchantApiKey(apiKey);
	}

	public Merchant save(Merchant merchant) {
		log.info("MerchantService - save: id=" + merchant.getId().toString());
		return repo.save(merchant);
	}

	public void delete(Integer id) {
		log.info("MerchantService - delete: id=" + id.toString());
		repo.deleteById(id);
	}

}
