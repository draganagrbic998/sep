package com.example.demo.utils;

import java.time.LocalDate;

import com.example.demo.model.Client;

public class Utils {

	public static boolean cardExpired(Client client) {
		String mm = client.getExpirationDate().split("/")[0];
		String yy = "20" + client.getExpirationDate().split("/")[1];
		LocalDate today = LocalDate.now();

		if (Integer.parseInt(yy) < today.getYear()
				|| (Integer.parseInt(yy) >= today.getYear() && Integer.parseInt(mm) < today.getMonthValue())) {
			return true;
		}

		return false;
	}

}
