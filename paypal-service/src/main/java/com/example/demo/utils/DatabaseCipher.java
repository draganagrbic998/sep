package com.example.demo.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.example.demo.model.BillingPlan;
import com.example.demo.model.Merchant;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.Subscription;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DatabaseCipher {

	private Cipher cipher;
	private SecretKey key;
	private IvParameterSpec ips;

	public String encrypt(String plainText) {
		if (plainText == null || plainText.isBlank())
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
		if (cipherText == null || cipherText.isBlank())
			return cipherText;
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
		o.setMerchantApiKey(this.encrypt(o.getMerchantApiKey()));
		return o;
	}

	public Order decrypt(Order o) {
		o.setPayPalOrderId(this.decrypt(o.getPayPalOrderId()));
		o.setMerchantApiKey(this.decrypt(o.getMerchantApiKey()));
		return o;
	}

	public Product encrypt(Product p) {
		p.setProductId(this.encrypt(p.getProductId()));
		p.setName(this.encrypt(p.getName()));
		return p;
	}

	public Product decrypt(Product p) {
		p.setProductId(this.decrypt(p.getProductId()));
		p.setName(this.decrypt(p.getName()));
		return p;
	}

	public BillingPlan encrypt(BillingPlan b) {
		b.setPlanId(this.encrypt(b.getPlanId()));
		b.setProductId(this.encrypt(b.getProductId()));
		b.setName(this.encrypt(b.getName()));
		return b;
	}

	public BillingPlan decrypt(BillingPlan b) {
		b.setPlanId(this.decrypt(b.getPlanId()));
		b.setProductId(this.decrypt(b.getProductId()));
		b.setName(this.decrypt(b.getName()));
		return b;
	}

	public Subscription encrypt(Subscription s) {
		s.setApproveUrl(this.encrypt(s.getApproveUrl()));
		s.setSubscriber(this.encrypt(s.getSubscriber()));
		s.setSubscriptionId(this.encrypt(s.getSubscriptionId()));
		s.setPlanId(this.encrypt(s.getPlanId()));
		return s;
	}

	public Subscription decrypt(Subscription s) {
		s.setApproveUrl(this.decrypt(s.getApproveUrl()));
		s.setSubscriber(this.decrypt(s.getSubscriber()));
		s.setSubscriptionId(this.decrypt(s.getSubscriptionId()));
		s.setPlanId(this.decrypt(s.getPlanId()));
		return s;
	}

}
