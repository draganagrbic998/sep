package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public Merchant save(Merchant merchant) {
		return repo.save(merchant);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}

}
