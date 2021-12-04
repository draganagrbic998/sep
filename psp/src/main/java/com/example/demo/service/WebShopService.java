package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.example.demo.model.WebShop;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@AllArgsConstructor
public class WebShopService {

	private final DiscoveryClient discoveryClient;

	public List<WebShop> read() {
		log.info("WebShopService - read");
		return discoveryClient.getServices().stream().filter(item -> item.startsWith("webshop"))
				.map(item -> new WebShop(item, "localhost:" + discoveryClient.getInstances(item).get(0).getPort()))
				.collect(Collectors.toList());
	}

}
