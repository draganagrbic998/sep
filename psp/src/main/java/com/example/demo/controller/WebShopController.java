package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.WebShop;
import com.example.demo.service.WebShopService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/webshops")
public class WebShopController {

	private final WebShopService service;

	@GetMapping
	public ResponseEntity<List<WebShop>> read() {
		log.info("WebShopController - read");
		return ResponseEntity.ok(service.read());
	}

	@GetMapping("/{id}")
	public ResponseEntity<WebShop> readOne(@PathVariable Long id) {
		log.info("WebShopController - readOne: id=" + id);
		return ResponseEntity.ok(service.readOne(id));
	}

	@PostMapping
	public ResponseEntity<WebShop> create(@Valid @RequestBody WebShop dto) {
		log.info("WebShopController - create");

		if (dto.getId() != null) {
			log.error("delete - dto id not null");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.save(dto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<WebShop> update(@PathVariable Long id, @Valid @RequestBody WebShop dto) {
		log.info("WebShopController - update: id=" + id);

		if (id == null || dto.getId() == null || id != dto.getId()) {
			log.error("delete - id is invalid");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.save(dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.info("WebShopController - delete: id=" + id);

		if (id == null) {
			log.error("delete - id is null");
			return ResponseEntity.badRequest().build();
		}
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
