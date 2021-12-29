package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final PropertiesData properties;

	@RequestMapping("/success")
	public String success() {
		return "success";
	}

	@RequestMapping("/fail")
	public String fail() {
		return "fail";
	}

	@RequestMapping("/error")
	public String error() {
		return "error";
	}

	@RequestMapping("/register")
	public String register(Model model) {
		model.addAttribute("registerUrl", properties.registerUrl);
		return "register";
	}

}
