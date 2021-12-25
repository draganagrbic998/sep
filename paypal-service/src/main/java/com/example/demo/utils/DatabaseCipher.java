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

	public Merchant encrypt(Merchant merchant) {
		merchant.setClientId(encrypt(merchant.getClientId()));
		merchant.setClientSecret(encrypt(merchant.getClientSecret()));
		merchant.setMerchantApiKey(encrypt(merchant.getMerchantApiKey()));
		return merchant;
	}

	public Merchant decrypt(Merchant merchant) {
		merchant.setClientId(decrypt(merchant.getClientId()));
		merchant.setClientSecret(decrypt(merchant.getClientSecret()));
		merchant.setMerchantApiKey(decrypt(merchant.getMerchantApiKey()));
		return merchant;
	}

	public Order encrypt(Order order) {
		order.setPayPalOrderId(encrypt(order.getPayPalOrderId()));
		order.setMerchantApiKey(encrypt(order.getMerchantApiKey()));
		return order;
	}

	public Order decrypt(Order order) {
		order.setPayPalOrderId(decrypt(order.getPayPalOrderId()));
		order.setMerchantApiKey(decrypt(order.getMerchantApiKey()));
		return order;
	}

	public Product encrypt(Product product) {
		product.setProductId(encrypt(product.getProductId()));
		product.setName(encrypt(product.getName()));
		return product;
	}

	public Product decrypt(Product product) {
		product.setProductId(decrypt(product.getProductId()));
		product.setName(decrypt(product.getName()));
		return product;
	}

	public BillingPlan encrypt(BillingPlan plan) {
		plan.setPlanId(encrypt(plan.getPlanId()));
		plan.setProductId(encrypt(plan.getProductId()));
		plan.setName(encrypt(plan.getName()));
		return plan;
	}

	public BillingPlan decrypt(BillingPlan plan) {
		plan.setPlanId(decrypt(plan.getPlanId()));
		plan.setProductId(decrypt(plan.getProductId()));
		plan.setName(decrypt(plan.getName()));
		return plan;
	}

	public Subscription encrypt(Subscription subscription) {
		subscription.setSubscriptionId(encrypt(subscription.getSubscriptionId()));
		subscription.setPlanId(encrypt(subscription.getPlanId()));
		subscription.setSubscriber(encrypt(subscription.getSubscriber()));
		subscription.setApproveUrl(encrypt(subscription.getApproveUrl()));
		return subscription;
	}

	public Subscription decrypt(Subscription subscription) {
		subscription.setSubscriptionId(decrypt(subscription.getSubscriptionId()));
		subscription.setPlanId(decrypt(subscription.getPlanId()));
		subscription.setSubscriber(decrypt(subscription.getSubscriber()));
		subscription.setApproveUrl(decrypt(subscription.getApproveUrl()));
		return subscription;
	}

}
