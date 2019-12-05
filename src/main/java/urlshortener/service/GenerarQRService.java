package urlshortener.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import urlshortener.exceptions.*;

@Service
public class GenerarQRService {
	// La clave por la cual se cachea es text, la anchura y la altura nos dan igual
	@Cacheable(value="qrs", key="#text")
	public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
		System.out.println("Generando QR del texto " + text);
		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
			System.out.println("Going to sleep for 5 Secs.. to simulate backend call.");
            Thread.sleep(1000*5);
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new QRNotGeneratedException("QR not generated");
		}	
	}
}