package urlshortener.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import urlshortener.exceptions.ConectionRefusedException;

@Service
public class ValidatorService {
    
    /** 
     * Valida una url
     * @param url
     * @return String
     * @throws IOException
     */
    public String validate(String url) throws IOException {
      boolean Constructable = false;
      OkHttpClient client = new OkHttpClient();
      Request request = new Request.Builder()
          .url(url)
          .build();
      Response response = client.newCall(request).execute();
      if(response.isSuccessful() == true){
        System.out.println(url);
        return "Constructable";
      }
        return null;
  }
    @CachePut(value = "qrs", key = "#hash")
    public void updateCache(String hash){}
}
