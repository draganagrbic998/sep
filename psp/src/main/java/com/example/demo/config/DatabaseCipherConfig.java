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

import com.example.demo.utils.DatabaseCipher;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class DatabaseCipherConfig {

	private final String keystoreFile = "databaseKeystore.jks";
	private final char[] keystorePassword = "pass".toCharArray();
	private final String ipsFile = "ips.bin";

	@Bean
	public DatabaseCipher getDatabaseCipher() {
		DatabaseCipher databaseCipher = null;
		SecretKey key = null;

		try {
			KeyStore keyStore = KeyStore.getInstance("JCEKS");
			InputStream in = new FileInputStream(ResourceUtils.getFile("classpath:" + keystoreFile));
			keyStore.load(in, keystorePassword);
			in.close();
			key = (SecretKey) keyStore.getKey("databaseKey", keystorePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (key == null) {
			try {
				KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
				keyGenerator.init(256);
				key = keyGenerator.generateKey();

				File file = ResourceUtils.getFile("classpath:" + keystoreFile);
				file.createNewFile();

				KeyStore keyStore = KeyStore.getInstance("JCEKS");
				keyStore.load(null, keystorePassword);
				keyStore.setEntry("databaseKey", new KeyStore.SecretKeyEntry(key),
						new KeyStore.PasswordProtection(keystorePassword));
				keyStore.store(new FileOutputStream(file), keystorePassword);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		IvParameterSpec ips = null;

		try {
			InputStream in = new FileInputStream(ResourceUtils.getFile("classpath:" + ipsFile));
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

				File file = ResourceUtils.getFile("classpath:" + ipsFile);
				file.createNewFile();

				FileOutputStream out = new FileOutputStream(file);
				out.write(iv);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			databaseCipher = new DatabaseCipher(Cipher.getInstance("AES/CBC/PKCS5Padding"), key, ips);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return databaseCipher;
	}

}
