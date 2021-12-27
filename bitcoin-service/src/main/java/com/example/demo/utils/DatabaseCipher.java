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
		if (plainText == null || plainText.isBlank()) {
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
		if (cipherText == null) {
			return cipherText;
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, ips);
			return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public Merchant encrypt(Merchant merchant) {
		merchant.setCoingateToken(encrypt(merchant.getCoingateToken()));
		merchant.setMerchantApiKey(encrypt(merchant.getMerchantApiKey()));
		return merchant;
	}

	public Merchant decrypt(Merchant merchant) {
		merchant.setCoingateToken(decrypt(merchant.getCoingateToken()));
		merchant.setMerchantApiKey(decrypt(merchant.getMerchantApiKey()));
		return merchant;
	}

	public Order encrypt(Order order) {
		order.setCoingateOrderId(encrypt(order.getCoingateOrderId()));
		order.setMerchantApiKey(encrypt(order.getMerchantApiKey()));
		return order;
	}

	public Order decrypt(Order order) {
		order.setCoingateOrderId(decrypt(order.getCoingateOrderId()));
		order.setMerchantApiKey(decrypt(order.getMerchantApiKey()));
		return order;
	}

}
