package com.example.demo.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.example.demo.model.Client;
import com.example.demo.model.PaymentRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DatabaseCipher {

	private Cipher cipher;
	private SecretKey key;
	private IvParameterSpec ips;

	public String encrypt(String plainText) {
		if (plainText.isBlank()) {
			return plainText;
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, ips);
			return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public String decrypt(String cipherText) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, ips);
			return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public Client encrypt(Client client) {
		client.setCardHolder(encrypt(client.getCardHolder()));
		client.setPanNumber(encrypt(client.getPanNumber()));
		client.setExpirationDate(encrypt(client.getExpirationDate()));
		client.setCvv(encrypt(client.getCvv()));
		client.setMerchantId(encrypt(client.getMerchantId()));
		client.setMerchantPassword(encrypt(client.getMerchantPassword()));
		return client;
	}

	public PaymentRequest encrypt(PaymentRequest request) {
		request.setMerchantId(encrypt(request.getMerchantId()));
		request.setMerchantPassword(encrypt(request.getMerchantPassword()));
		return request;
	}

}
