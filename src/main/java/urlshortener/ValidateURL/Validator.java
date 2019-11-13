package urlshortener.ValidateURL;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Validator {
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
            System.out.println(code);
		} catch (Exception e) {
            System.out.println("Conection refused");
        }
		return result;
	}
}