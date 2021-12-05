package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ViewController {

	@RequestMapping("/registerForm")
	public String registerForm() {
		return "registerForm";
	}

	@RequestMapping("/paymentForm/{paymentRequestId}")
	public String form() {
		return "paymentForm";
	}

}
