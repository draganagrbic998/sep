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

	public Transaction encrypt(Transaction t) {
		t.setMerchantId(this.encrypt(t.getMerchantId()));
		t.setPanNumber(this.encrypt(t.getPanNumber()));
		return t;
	}

	public Transaction decrypt(Transaction t) {
		t.setMerchantId(this.decrypt(t.getMerchantId()));
		t.setPanNumber(this.decrypt(t.getPanNumber()));
		return t;
	}

	public PaymentRequest encrypt(PaymentRequest pr) {
		pr.setMerchantId(this.encrypt(pr.getMerchantId()));
		pr.setMerchantPassword(this.encrypt(pr.getMerchantPassword()));
		return pr;
	}

	public PaymentRequest decrypt(PaymentRequest pr) {
		pr.setMerchantId(this.decrypt(pr.getMerchantId()));
		pr.setMerchantPassword(this.decrypt(pr.getMerchantPassword()));
		return pr;
	}

	public Client encrypt(Client c) {
		c.setMerchantId(this.encrypt(c.getMerchantId()));
		c.setMerchantPassword(this.encrypt(c.getMerchantPassword()));
		c.setPanNumber(this.encrypt(c.getPanNumber()));
		c.setCardHolder(this.encrypt(c.getCardHolder()));
		c.setCvv(this.encrypt(c.getCvv()));
		c.setExpirationDate(this.encrypt(c.getExpirationDate()));
		return c;
	}

	public Client decrypt(Client c) {
		c.setMerchantId(this.decrypt(c.getMerchantId()));
		c.setMerchantPassword(this.decrypt(c.getMerchantPassword()));
		c.setPanNumber(this.decrypt(c.getPanNumber()));
		c.setCardHolder(this.decrypt(c.getCardHolder()));
		c.setCvv(this.decrypt(c.getCvv()));
		c.setExpirationDate(this.decrypt(c.getExpirationDate()));
		return c;
	}

}
