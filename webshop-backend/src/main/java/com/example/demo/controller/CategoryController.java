package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryService service;

	@GetMapping
	public ResponseEntity<List<Category>> read() {
		log.info("CategoryController - read");
		return ResponseEntity.ok(service.read());
	}

}
