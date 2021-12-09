package com.example.demo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import com.example.demo.utils.PropertiesData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RateService {

	private final PropertiesData properties;

	public Double getCurrencyRate(String currency) {
		currency = currency.toLowerCase();
		if (currency.equals("rsd")) {
			return 1.0;
		}

		return new Gson().fromJson(getRate(), JsonObject.class).get("result").getAsJsonObject().get(currency)
				.getAsJsonObject().get("sre").getAsDouble();
	}

	private String getRate() {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet get = new HttpGet("http://api.kursna-lista.info/".concat(properties.nbsApi).concat("/kl_na_dan/")
					.concat(DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.now())).concat("/kursna_lista"));
			get.setHeader("Accept", "application/json");
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() != 200) {
				return "";
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line;
			StringBuffer buffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
