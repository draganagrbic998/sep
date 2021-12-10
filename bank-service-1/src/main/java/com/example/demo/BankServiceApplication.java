package com.example.demo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.utils.DatabaseCipher;

@SpringBootApplication
public class BankServiceApplication {

	@Autowired
	private DatabaseCipher cipher;

	@PostConstruct
	public void test() {
		System.out.println(cipher.encrypt("192112"));
		System.out.println(cipher.encrypt("https://localhost:8090"));
		System.out.println("====================================");

		System.out.println(cipher.encrypt("192113"));
		System.out.println(cipher.encrypt("https://localhost:8091"));
		System.out.println("====================================");

	}

	public static void main(String[] args) {
		SpringApplication.run(BankServiceApplication.class, args);
	}

}
