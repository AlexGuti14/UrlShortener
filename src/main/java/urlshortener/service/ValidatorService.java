package urlshortener.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import urlshortener.exceptions.ConectionRefusedException;

@Service
public class ValidatorService {
    public String validate(String url) throws IOException {

      HashMap<Integer, String> HTTPcodes = new HashMap<Integer, String>();
      HTTPcodes.put(400,"400: Bad Request");
      HTTPcodes.put(401,"401: Unauthorized");
      HTTPcodes.put(403,"403: Forbidden");
      HTTPcodes.put(409,"409: Conflict");
      HTTPcodes.put(410,"410: Gone");
      HTTPcodes.put(500,"500: Internal Server Error");

  		String result;
  		int code;

  		try {
        URL siteURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
  			connection.setRequestMethod("GET");
  			connection.setConnectTimeout(1000);
        connection.connect();

  			code = connection.getResponseCode();
  			if (code >= 200 && code < 400) {
  				result = "Constructable";
          System.out.println(code);
  			}
        else{
          result = HTTPcodes.get(code);
        }
  		} catch (IOException e) {
            throw new ConectionRefusedException("404: Not Found");

          }
  		return result;
  	}
}
