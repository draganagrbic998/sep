package com.example.demo.config;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.demo.example.exception.RestTemplateMessageException;
import com.demo.example.exception.RestTemplateVoidException;
import com.example.demo.dto.Error;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import lombok.SneakyThrows;

@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
		return httpResponse.getStatusCode().series() == CLIENT_ERROR
				|| httpResponse.getStatusCode().series() == SERVER_ERROR;
	}

	@Override
	@SneakyThrows
	public void handleError(ClientHttpResponse httpResponse) {
		try {
			Gson gson = new Gson();
			InputStreamReader bodyReader = new InputStreamReader(httpResponse.getBody());
			throw new RestTemplateMessageException(gson.fromJson(bodyReader, Error.class));
		} catch (JsonIOException | JsonSyntaxException ex) {
			throw new RestTemplateVoidException();
		}

	}
}
