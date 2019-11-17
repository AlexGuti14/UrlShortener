package urlshortener.web;

import org.apache.commons.validator.routines.UrlValidator;
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

import javax.servlet.http.HttpServletRequest;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class UrlShortenerController {
    private final ShortURLService shortUrlService;

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
        ValidatorService v = new ValidatorService();
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
    public void SaveCSV(@RequestParam("linklist") List<String> linklist,
            @RequestParam(value = "sponsor", required = false) String sponsor, HttpServletRequest request)
            throws IOException {
        System.out.println("List recieved!");
        ShortURL su = new ShortURL();
        HttpHeaders h = new HttpHeaders();
        ValidatorService v = new ValidatorService();
        for (int i = 0; i < linklist.size(); i++) {
            linklist.set(i, linklist.get(i).replace("\"", ""));
            linklist.set(i, linklist.get(i).replace("[", ""));
            linklist.set(i, linklist.get(i).replace("]", ""));
            System.out.println(linklist.get(i));
            UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });
            if (urlValidator.isValid(linklist.get(i)) && v.validate(linklist.get(i))) {
                su = shortUrlService.save(linklist.get(i), sponsor, request.getRemoteAddr());
                h.setLocation(su.getUri());
            }
        }
    }

    // Funcion Listar
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<ShortURL>> listar(HttpServletRequest request) throws WriterException, IOException {
        System.out.println("Ejecucion listar de URLShortenerController");
        List<ShortURL> aDevolver = shortUrlService.list(100L, 0L);

        GenerarQRService qr = new GenerarQRService();

        Iterator<ShortURL> nombreIterator = aDevolver.iterator();
        while(nombreIterator.hasNext()){
            ShortURL elemento = nombreIterator.next();
            elemento.setClicks(clickService.clicksByHash(elemento.getHash()));
            elemento.setQR(qr.getQRCodeImage(elemento.getHash(), 150, 150));
        }

        return new ResponseEntity<>(aDevolver, HttpStatus.CREATED);
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
