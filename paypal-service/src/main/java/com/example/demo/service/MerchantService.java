package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.Merchant;
import com.example.demo.repository.MerchantRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class MerchantService {

	private final MerchantRepository repo;

	public Merchant save(Merchant merchant) {
		log.info("MerchantService - save: id=" + merchant.getId());
		return repo.save(merchant);
	}

}
