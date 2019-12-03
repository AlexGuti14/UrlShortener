package urlshortener.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Service;
import urlshortener.service.ShortURLService;
import urlshortener.domain.ShortURL;
import urlshortener.exceptions.ConectionRefusedException;
import urlshortener.repository.ShortURLRepository;

@Service
public class ValidatorService {
    public boolean validate(String url) throws IOException {
		boolean result = false;
		int code;
		try {
			URL siteURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(1000);
			connection.connect();
 
			code = connection.getResponseCode();
			if (code == 200) {
				result = true;
			}
			// GESTIONAR DEMAS CODIGOS DE RESPUESTA
            System.out.println(code);
		} catch (Exception e) {
			System.out.println("Conection refused");
			throw new ConectionRefusedException("ERROR, URI is unreachable");
			
        }
		return result;
	}
}