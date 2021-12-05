package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ViewController {

	@RequestMapping("/success_url")
	public String successURL(Model model) {
		return "successful";
	}

	@RequestMapping("/failed_url")
	public String failedURL(Model model) {
		return "failed";
	}

	@RequestMapping("/error_url")
	public String errorURL(Model model) {
		return "error";
	}

	@RequestMapping("/register")
	public String form(Model model) {
		return "register";
	}

}
