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

	public Optional<Merchant> findByMerchantWebshopIdOpional(Integer mechantWebshopId) {
		return repo.findByMerchantWebshopId(mechantWebshopId);
	}

	public Merchant findByMerchantWebshopId(Integer mechantWebshopId) throws NotFoundException {
		Optional<Merchant> merchant = repo.findByMerchantWebshopId(mechantWebshopId);

		if (!merchant.isPresent()) {
			throw new NotFoundException(mechantWebshopId.toString(), Merchant.class.getSimpleName());
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
