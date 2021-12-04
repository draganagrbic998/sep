package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.example.exception.NotFoundException;
import com.example.demo.model.PaymentMethod;
import com.example.demo.model.WebShop;
import com.example.demo.repo.WebShopRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@AllArgsConstructor
public class WebShopService {

	private final WebShopRepository repo;

	@Transactional(readOnly = true)
	public List<WebShop> read() {
		log.info("WebShopService - read");
		return repo.findAll();
	}

	@Transactional(readOnly = true)
	public WebShop readOne(Long id) {
		log.info("WebShopService - readOne: id=" + id);
		return repo.findById(id).get();
	}

	@Transactional
	public WebShop save(WebShop webShop) {
		webShop = repo.save(webShop);
		log.info("WebShopService - save: id=" + webShop.getId());
		return webShop;
	}

	@Transactional
	public void delete(Long id) {
		log.info("WebShopService - delete: id=" + id);
		Optional<WebShop> webshop = repo.findById(id);

		if (!webshop.isPresent()) {
			log.error("WebShop: id=" + id + " not found.");
			throw new NotFoundException(id.toString(), PaymentMethod.class.getSimpleName());
		}

		repo.deleteById(id);
	}

}
