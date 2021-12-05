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
		m.setClientId(this.encrypt(m.getClientId()));
		m.setClientSecret(this.encrypt(m.getClientSecret()));
		m.setMerchantApiKey(this.encrypt(m.getMerchantApiKey()));
		return m;
	}

	public Merchant decrypt(Merchant m) {
		m.setClientId(this.decrypt(m.getClientId()));
		m.setClientSecret(this.decrypt(m.getClientSecret()));
		m.setMerchantApiKey(this.decrypt(m.getMerchantApiKey()));
		return m;
	}

	public Order encrypt(Order o) {
		o.setPayPalOrderId(this.encrypt(o.getPayPalOrderId()));
		return o;
	}

	public Order decrypt(Order o) {
		o.setPayPalOrderId(this.decrypt(o.getPayPalOrderId()));
		return o;
	}

}
