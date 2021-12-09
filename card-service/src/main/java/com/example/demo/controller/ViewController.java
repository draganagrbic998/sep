package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ViewController {

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

}
