package urlshortener.web;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import urlshortener.domain.ShortURL;
import urlshortener.QR.GenerarQR;
import urlshortener.domain.Click;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;


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
            @RequestParam(value = "sponsor", required = false) String sponsor, HttpServletRequest request) {
        UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });
        if (urlValidator.isValid(url)) {
            ShortURL su = shortUrlService.save(url, sponsor, request.getRemoteAddr());
            HttpHeaders h = new HttpHeaders();
            h.setLocation(su.getUri());
            return new ResponseEntity<>(su, h, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Funcion Listar
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<ShortURL>> listar(HttpServletRequest request) throws WriterException, IOException {
        System.out.println("Ejecucion listar de URLShortenerController");
        List<ShortURL> aDevolver = shortUrlService.list(100L, 0L);

        GenerarQR qr = new GenerarQR();

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
