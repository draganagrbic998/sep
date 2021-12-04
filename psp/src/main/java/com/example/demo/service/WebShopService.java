package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.WebShop;
import com.example.demo.repo.WebShopRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@AllArgsConstructor
public class WebShopService {

	private final WebShopRepository repo;

	public List<WebShop> read() {
		log.info("WebShopService - read");
		return repo.findAll();
	}

	public WebShop save(WebShop webShop) {
		webShop = repo.save(webShop);
		log.info("WebShopService - save: id=" + webShop.getId());
		return webShop;
	}

	public void delete(Long id) {
		log.info("WebShopService - remove: id=" + id);
		repo.deleteById(id);
	}

}
