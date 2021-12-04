package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Role;
import com.example.demo.repo.RoleRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@AllArgsConstructor
public class RoleService {

	private final RoleRepository repo;

	@Transactional(readOnly = true)
	public List<Role> read() {
		log.info("RoleService - read");
		return repo.findAll();
	}

}
