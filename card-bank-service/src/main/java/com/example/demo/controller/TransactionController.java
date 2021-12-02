package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.example.exception.NotFoundException;
import com.example.demo.service.TransactionService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	private ResponseEntity<String> getTransaction(@PathVariable Integer orderId) throws NotFoundException {
		log.info("TransactionController - getTransaction: orderId=" + orderId.toString());
		return new ResponseEntity<>(transactionService.findByMerchantOrderId(orderId).getStatus().toString(),
				HttpStatus.OK);
	}

}
