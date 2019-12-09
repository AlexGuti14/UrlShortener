package urlshortener.service;

import java.io.IOException;
import com.google.zxing.WriterException;
import urlshortener.repository.QRRepository;
import org.springframework.stereotype.Service;

@Service
public class GenerarQRService {

	private final QRRepository QRRepository;

    public GenerarQRService(QRRepository QRRepository) {
        this.QRRepository = QRRepository;
	}
	
	public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
		return QRRepository.getQR(text, width, height);
	}
}