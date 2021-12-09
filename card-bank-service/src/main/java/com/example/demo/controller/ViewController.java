package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.utils.PropertiesData;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final PropertiesData properties;

	@RequestMapping("/payment/{requestId}")
	public String payment(@PathVariable Long requestId, Model model) {
		model.addAttribute("requestId", requestId);
		model.addAttribute("confirmUrl", properties.confirmUrl);
		return "payment";
	}

}
