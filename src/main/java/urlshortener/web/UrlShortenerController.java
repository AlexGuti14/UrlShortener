package urlshortener.web;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import urlshortener.domain.ShortURL;
import urlshortener.domain.Click;
import urlshortener.service.ClickService;
import urlshortener.service.GenerarQRService;
import urlshortener.service.ShortURLService;
import urlshortener.service.ValidatorService;

import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;


import javax.servlet.http.HttpServletRequest;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class UrlShortenerController {
    private final ShortURLService shortUrlService;
    private final ValidatorService v = new ValidatorService();
    private final GenerarQRService qr = new GenerarQRService();
    private final ClickService clickService;

    public UrlShortenerController(ShortURLService shortUrlService, ClickService clickService) {
        this.shortUrlService = shortUrlService;
        this.clickService = clickService;
    }

    @RequestMapping(value = "/{id:(?!link|index).*}", method = RequestMethod.GET)
    public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
        ShortURL l = shortUrlService.findByKey(id);
        if (l != null) {
            clickService.saveClick(id, extractIP(request));
            return createSuccessfulRedirectToResponse(l);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/link", method = RequestMethod.POST)
    public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
            @RequestParam(value = "sponsor", required = false) String sponsor, HttpServletRequest request)
            throws IOException {
        UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });
        
        if (urlValidator.isValid(url) && v.validate(url)) {
            ShortURL su = shortUrlService.save(url, sponsor, request.getRemoteAddr());
            HttpHeaders h = new HttpHeaders();
            h.setLocation(su.getUri());
            return new ResponseEntity<>(su, h, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Función para cargar CSV a base de datos
    @RequestMapping(value = "/csv", method = RequestMethod.POST)
    public void SaveCSV(@RequestParam("linklist[]") String[] linklist,
            @RequestParam(value = "sponsor", required = false) String sponsor, HttpServletRequest request)
            throws IOException {
        ShortURL su = new ShortURL();
        HttpHeaders h = new HttpHeaders();
        for (int i = 0; i < linklist.length; i++) {
            UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });
            if (urlValidator.isValid(linklist[i]) && v.validate(linklist[i])) {
                su = shortUrlService.save(linklist[i], sponsor, request.getRemoteAddr());
            }
        }
    }

    // Funcion Listar
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<ShortURL>> listar(HttpServletRequest request) throws WriterException, IOException {
        System.out.println("Ejecucion listar de URLShortenerController");
        List<ShortURL> aDevolver = shortUrlService.list(100L, 0L);

        aDevolver.forEach(item->{
            item.setClicks(clickService.clicksByHash(item.getHash()));
            try {
                item.setQR(qr.getQRCodeImage(item.getUri().toString(), 150, 150));
            } catch (WriterException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        return new ResponseEntity<>(aDevolver, HttpStatus.CREATED);
    }

    // TODO: Lanza NullPointerException sin parar pero cuando tiene que hacer la comprobacion la hace bien¡
    // TODO: A la hora de cachear mirar qeu la cache este a la par con la base de datos, así como intentar ponerle un tiempo
    // maximo de expiracion a los datos o una politica de expiraion
    @Scheduled(fixedDelay = 50000 ) // Function will be executed X time after the last one finishes
    //@CacheEvict(¿key = (elemento.getUri().toString()??)
	public void VerificacionPeriodica() throws IOException {
        List<ShortURL> aDevolver = shortUrlService.list(100L, 0L);
        if (!aDevolver.isEmpty()){
            for (ShortURL elemento: aDevolver){
                // Check if the URI is reachable, delete from database if not.
                if(!v.validate(elemento.getUri().toString())){
                    shortUrlService.delete(elemento.getHash());
                }
            }
        }
	}

    @RequestMapping(value = "/qr", method = RequestMethod.GET)
    public ResponseEntity<byte[]> crearQr (@RequestParam("hash") String hash) throws IOException, WriterException {
        System.out.println("Hash para cread el qr: " + hash);
        return new ResponseEntity<>(qr.getQRCodeImage(hash, 150, 150), HttpStatus.CREATED);
    }

    private String extractIP(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    private ResponseEntity<?> createSuccessfulRedirectToResponse(ShortURL l) {
        HttpHeaders h = new HttpHeaders();
        h.setLocation(URI.create(l.getTarget()));
        return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
    }
}
