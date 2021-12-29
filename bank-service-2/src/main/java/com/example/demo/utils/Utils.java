package com.example.demo.utils;

import java.time.LocalDate;

public class Utils {

	public static boolean cardExpired(String expirationDate) {
		String yy = "20" + expirationDate.split("/")[1];
		String mm = expirationDate.split("/")[0];
		LocalDate today = LocalDate.now();

		return Integer.parseInt(yy) < today.getYear()
				|| (Integer.parseInt(yy) >= today.getYear() && Integer.parseInt(mm) < today.getMonthValue());

	}

}
