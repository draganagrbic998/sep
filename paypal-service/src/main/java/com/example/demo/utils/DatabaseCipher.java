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
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, ips);
			return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public Merchant encrypt(Merchant merchant) {
		merchant.setMerchantApiKey(encrypt(merchant.getMerchantApiKey()));
		merchant.setClientId(encrypt(merchant.getClientId()));
		merchant.setClientSecret(encrypt(merchant.getClientSecret()));
		return merchant;
	}

	public Order encrypt(Order order) {
		order.setPayPalOrderId(encrypt(order.getPayPalOrderId()));
		order.setMerchantApiKey(encrypt(order.getMerchantApiKey()));
		return order;
	}

	public Product encrypt(Product product) {
		product.setProductId(encrypt(product.getProductId()));
		product.setName(encrypt(product.getName()));
		return product;
	}

	public BillingPlan encrypt(BillingPlan plan) {
		plan.setProductId(encrypt(plan.getProductId()));
		plan.setPlanId(encrypt(plan.getPlanId()));
		plan.setName(encrypt(plan.getName()));
		return plan;
	}

	public Subscription encrypt(Subscription subscription) {
		subscription.setPlanId(encrypt(subscription.getPlanId()));
		subscription.setSubscriptionId(encrypt(subscription.getSubscriptionId()));
		subscription.setSubscriber(encrypt(subscription.getSubscriber()));
		subscription.setApproveUrl(encrypt(subscription.getApproveUrl()));
		return subscription;
	}

}
