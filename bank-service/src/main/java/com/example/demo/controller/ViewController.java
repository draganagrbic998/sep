package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/view")
public class ViewController {

	@RequestMapping(value = "/form/{paymentRequestId}")
	public String form(@PathVariable Integer paymentRequestId, Model model) {
		model.addAttribute("paymentRequestId", paymentRequestId);
		return "form";
	}

}
