package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.example.demo.dto.RateDTO;

@Service
public class KursnaListaService {

	@Value("${nbsAPI}")
	private String nbsAPI;

	public RateDTO findRate(String currency, String rateDate) throws IOException {
		if (currency.equalsIgnoreCase("rsd")) {
			RateDTO result = new RateDTO();
			result.setCurrency(currency);
			result.setValue(1f);
			return result;
		}

		String kursnaLista = getKursnaLista(rateDate);
		JsonObject jsonObject = new Gson().fromJson(kursnaLista, JsonObject.class);
		RateDTO result = new RateDTO();
		result.setCurrency(currency);
		result.setValue(
				jsonObject.get("result").getAsJsonObject().get(currency).getAsJsonObject().get("sre").getAsFloat());
		return result;

	}

	private String getKursnaLista(String rateDate) throws IOException {
		String url = "http://api.kursna-lista.info/".concat(nbsAPI).concat("/kl_na_dan/").concat(rateDate)
				.concat("/kursna_lista");

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		get.setHeader("Accept", "application/json");

		HttpResponse response = client.execute(get);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		String inputLine;
		StringBuffer responsec = new StringBuffer();

		while ((inputLine = rd.readLine()) != null) {
			responsec.append(inputLine);
		}
		rd.close();

		if (response.getStatusLine().getStatusCode() == 200) {
			return responsec.toString();
		}
		return "";
	}

}
