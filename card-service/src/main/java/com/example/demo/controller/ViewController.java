package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ViewController {

	@RequestMapping(value = "/success_url")
	public String successURL(Model model) {
		return "successful";
	}

	@RequestMapping(value = "/failed_url")
	public String failedURL(Model model) {
		return "failed";
	}

	@RequestMapping(value = "/error_url")
	public String errorURL(Model model) {
		return "error";
	}

	@RequestMapping(value = "/register/{merchantId}")
	public String form(@PathVariable Integer merchantId, Model model) {
		model.addAttribute("merchantId", merchantId);
		return "register";
	}

}
