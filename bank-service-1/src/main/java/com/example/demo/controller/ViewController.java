package com.example.demo.controller;

import java.util.Base64;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.utils.PropertiesData;
import com.example.demo.utils.QRCodeGenerator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/view")
public class ViewController {

	private final PropertiesData properties;

	@RequestMapping("/payment/{requestId}")
	public String payment(@PathVariable Long requestId, @RequestParam Boolean qr, Model model) {
		model.addAttribute("requestId", requestId);
		model.addAttribute("confirmUrl", properties.confirmUrl);
		if (!qr) {
			return "payment";
		}

		try {
			model.addAttribute("qrcode",
					Base64.getEncoder().encodeToString(QRCodeGenerator.generateQRCode(requestId + "", 250, 250)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "qr";
	}

	@RequestMapping("/register")
	public String register(Model model) {
		model.addAttribute("registerUrl", properties.registerUrl);
		return "register";
	}

}
