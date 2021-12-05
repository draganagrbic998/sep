package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ViewController {

	@RequestMapping("/form/{paymentRequestId}")
	public String form(@PathVariable Long paymentRequestId, Model model) {
		model.addAttribute("paymentRequestId", paymentRequestId);
		return "form";
	}

	@RequestMapping("/registerForm")
	public String form(Model model) {
		return "registerForm";
	}

}
