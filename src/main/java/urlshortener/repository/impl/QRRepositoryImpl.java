package urlshortener.repository.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import urlshortener.exceptions.QRNotGeneratedException;
import urlshortener.repository.QRRepository;
import java.io.ByteArrayOutputStream;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Repository
public class QRRepositoryImpl implements QRRepository {

    @Override
    @Cacheable("qrs")
    public 	byte[] getQR(String text, int width, int height) {
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