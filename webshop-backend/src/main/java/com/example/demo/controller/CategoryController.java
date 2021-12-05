package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
@PreAuthorize("hasAuthority('merchant')")
@Log4j2
public class CategoryController {

	private final CategoryService service;

	@GetMapping
	public ResponseEntity<List<Category>> read() {
		log.info("CategoryController - read");
		return ResponseEntity.ok(service.read());
	}

}
