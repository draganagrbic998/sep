package com.example.demo.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.example.demo.utils.CipherProperties;
import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class DatabaseCiperConfig {

	private final CipherProperties cipherProperties;

	@Bean
	public DatabaseCipher getDatabaseCipher() {
		DatabaseCipher databaseCipher = null;
		SecretKey key = null;

		try {
			KeyStore keyStore = KeyStore.getInstance("JCEKS");
			InputStream in = new FileInputStream(new File(this.cipherProperties.getDbKeystorePath()));
			keyStore.load(in, this.cipherProperties.getDbKeystorePassword().toCharArray());
			in.close();
			key = (SecretKey) keyStore.getKey("databaseKey",
					this.cipherProperties.getDbKeystorePassword().toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (key == null) {
			try {
				KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
				keyGenerator.init(256);
				key = keyGenerator.generateKey();

				File file = new File(this.cipherProperties.getDbKeystorePath());
				file.createNewFile();

				KeyStore keyStore = KeyStore.getInstance("JCEKS");
				keyStore.load(null, this.cipherProperties.getDbKeystorePassword().toCharArray());

				KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(
						this.cipherProperties.getDbKeystorePassword().toCharArray());

				KeyStore.SecretKeyEntry keyEntry = new KeyStore.SecretKeyEntry(key);
				keyStore.setEntry("databaseKey", keyEntry, protParam);
				keyStore.store(new FileOutputStream(file), this.cipherProperties.getDbKeystorePassword().toCharArray());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		IvParameterSpec ips = null;

		try {
			InputStream in = new FileInputStream(ResourceUtils.getFile(this.cipherProperties.getIpsPath()));
			ips = new IvParameterSpec(in.readAllBytes());
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (ips == null) {
			try {
				byte[] iv = new byte[16];
				new SecureRandom().nextBytes(iv);
				ips = new IvParameterSpec(iv);

				File file = new File(this.cipherProperties.getIpsPath());
				file.createNewFile();

				FileOutputStream out = new FileOutputStream(file);
				out.write(iv);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			databaseCipher = new DatabaseCipher(cipher, key, ips);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return databaseCipher;
	}

}
