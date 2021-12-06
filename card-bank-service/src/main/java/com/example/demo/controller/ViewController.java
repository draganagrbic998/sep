package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ViewController {

	@RequestMapping("/register")
	public String register() {
		return "register";
	}

	@RequestMapping("/payment/{paymentRequestId}")
	public String payment() {
		return "payment";
	}

}
