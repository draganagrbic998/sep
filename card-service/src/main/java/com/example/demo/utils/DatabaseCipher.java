package com.example.demo.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.example.demo.model.Merchant;
import com.example.demo.model.Order;

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

	public Merchant encrypt(Merchant m) {
		// m.setMerchantId(encrypt(m.getMerchantId()));
		// m.setMerchantPassword(encrypt(m.getMerchantPassword()));
		// m.setMerchantApiKey(encrypt(m.getMerchantApiKey()));
		// m.setBankUrl(encrypt(m.getBankUrl()));
		return m;
	}

	public Merchant decrypt(Merchant m) {
		// m.setMerchantId(decrypt(m.getMerchantId()));
		// m.setMerchantPassword(decrypt(m.getMerchantPassword()));
		// m.setMerchantApiKey(decrypt(m.getMerchantApiKey()));
		// m.setBankUrl(decrypt(m.getBankUrl()));
		return m;
	}

	public Order encrypt(Order o) {
		return o;
	}

	public Order decrypt(Order o) {
		return o;
	}

}
