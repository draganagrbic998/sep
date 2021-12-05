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
			this.cipher.init(Cipher.ENCRYPT_MODE, this.key, this.ips);
			return Base64.getEncoder().encodeToString(this.cipher.doFinal(plainText.getBytes()));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public String decrypt(String cipherText) {
		try {
			this.cipher.init(Cipher.DECRYPT_MODE, this.key, this.ips);
			return new String(this.cipher.doFinal(Base64.getDecoder().decode(cipherText)));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public Merchant encrypt(Merchant m) {
		m.setMerchantId(this.encrypt(m.getMerchantId()));
		m.setMerchantPassword(this.encrypt(m.getMerchantPassword()));
		m.setMerchantApiKey(this.encrypt(m.getMerchantApiKey()));
		m.setBankUrl(this.encrypt(m.getBankUrl()));
		return m;
	}

	public Merchant decrypt(Merchant m) {
		m.setMerchantId(this.decrypt(m.getMerchantId()));
		m.setMerchantPassword(this.decrypt(m.getMerchantPassword()));
		m.setMerchantApiKey(this.decrypt(m.getMerchantApiKey()));
		m.setBankUrl(this.decrypt(m.getBankUrl()));
		return m;
	}

	public Order encrypt(Order o) {
		return o;
	}

	public Order decrypt(Order o) {
		return o;
	}

}
