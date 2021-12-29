package com.example.demo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {

	public static byte[] generateQRCode(String text, int width, int height) throws WriterException, IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height), "PNG",
				result, new MatrixToImageConfig(0xFF000002, 0xFFFFC041));
		return result.toByteArray();
	}

}
