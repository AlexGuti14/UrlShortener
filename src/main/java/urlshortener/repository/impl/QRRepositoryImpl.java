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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Repository
public class QRRepositoryImpl implements QRRepository {

	private static final Logger LOG = LoggerFactory.getLogger(QRRepositoryImpl.class);

    @Override
    @Cacheable(cacheNames = "qrs", key = "#text")
    public 	byte[] getQR(String text, int width, int height) {
        try {

			LOG.info("Saving qr with ehcache with hash {} ",text);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new QRNotGeneratedException("QR not generated");
		}	        
    }
}