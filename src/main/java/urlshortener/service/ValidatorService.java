package urlshortener.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import urlshortener.service.ShortURLService;
import urlshortener.domain.ShortURL;
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
        }
		return result;
	}

	@Async
	@Scheduled(fixedDelay = 5000) // Function will be executed X time after the last one finishes
	public CompletableFuture<List<ShortURL>> VerificacionPeriodica(){
		ShortURLRepository repo = new ShortURLRepository();
		ShortURLService servicio = new ShortURLService(repo);
		List<ShortURL> aDevolver = servicio.list(100L, 0L);
		for (ShortURL elemento: aDevolver){
			// Check if the URI is reachable, delete from database if not.
			if(!validate(elemento.getUri().toString())){
				servicio.delete(elemento.getHash());
			}
		}
	}
}