package com.example.demo.utils;

import java.time.LocalDate;

import com.example.demo.model.Client;

public class Utils {

	public static boolean cardExpired(Client client) {
		String yy = "20" + client.getExpirationDate().split("/")[1];
		String mm = client.getExpirationDate().split("/")[0];
		LocalDate today = LocalDate.now();

		return Integer.parseInt(yy) < today.getYear()
				|| (Integer.parseInt(yy) >= today.getYear() && Integer.parseInt(mm) < today.getMonthValue());

	}

}
