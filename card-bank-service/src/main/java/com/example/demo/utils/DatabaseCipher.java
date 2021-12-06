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
import com.example.demo.model.Transaction;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DatabaseCipher {

	private Cipher cipher;
	private SecretKey key;
	private IvParameterSpec ips;

	public String encrypt(String plainText) {
		if (plainText.isBlank())
			return plainText;
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

	public Transaction encrypt(Transaction transaction) {
		transaction.setMerchantId(encrypt(transaction.getMerchantId()));
		// transaction.setPanNumber(encrypt(transaction.getPanNumber()));
		return transaction;
	}

	public Transaction decrypt(Transaction transaction) {
		transaction.setMerchantId(decrypt(transaction.getMerchantId()));
		// transaction.setPanNumber(decrypt(transaction.getPanNumber()));
		return transaction;
	}

	public PaymentRequest encrypt(PaymentRequest request) {
		// pr.setMerchantId(encrypt(pr.getMerchantId()));
		// pr.setMerchantPassword(encrypt(pr.getMerchantPassword()));
		return request;
	}

	public PaymentRequest decrypt(PaymentRequest request) {
		// pr.setMerchantId(decrypt(pr.getMerchantId()));
		// pr.setMerchantPassword(decrypt(pr.getMerchantPassword()));
		return request;
	}

	public Client encrypt(Client client) {
		client.setCardHolder(encrypt(client.getCardHolder()));
		client.setPanNumber(encrypt(client.getPanNumber()));
		client.setCvv(encrypt(client.getCvv()));
		client.setExpirationDate(encrypt(client.getExpirationDate()));

		client.setMerchantId(encrypt(client.getMerchantId()));
		client.setMerchantPassword(encrypt(client.getMerchantPassword()));
		return client;
	}

	public Client decrypt(Client client) {
		client.setCardHolder(decrypt(client.getCardHolder()));
		client.setPanNumber(decrypt(client.getPanNumber()));
		client.setCvv(decrypt(client.getCvv()));
		client.setExpirationDate(decrypt(client.getExpirationDate()));

		client.setMerchantId(decrypt(client.getMerchantId()));
		client.setMerchantPassword(decrypt(client.getMerchantPassword()));
		return client;
	}

}
