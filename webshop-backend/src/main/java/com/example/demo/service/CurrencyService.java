package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Currency;
import com.example.demo.repository.CurrencyRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CurrencyService {

	private final CurrencyRepository repo;

	@Transactional(readOnly = true)
	public List<Currency> read() {
		return repo.findAll();
	}

}
