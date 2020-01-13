package urlshortener.web;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import urlshortener.domain.ShortURL;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import urlshortener.service.ClickService;
import urlshortener.service.GenerarQRService;
import urlshortener.service.ShortURLService;
import urlshortener.service.ValidatorService;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class UrlShortenerController {

    private final ShortURLService shortUrlService;
    private final ValidatorService validatorService;
    private final GenerarQRService qr;
    private final ClickService clickService;

    public UrlShortenerController(ShortURLService shortUrlService, ClickService clickService, ValidatorService validatorService, GenerarQRService qr) {
        this.shortUrlService = shortUrlService;
        this.clickService = clickService;
        this.validatorService = validatorService;
        this.qr = qr;
    }

    
    /** 
     * Función que suma click a una url
     * @param id
     * @param request
     * @return ResponseEntity<?>
     */
    @RequestMapping(value = "/{id:(?!link|index).*}", method = RequestMethod.GET)
    public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
        ShortURL l = shortUrlService.findByKey(id);
        System.out.println(id);
        if (l != null) {
            clickService.saveClick(id, extractIP(request));
            return createSuccessfulRedirectToResponse(l);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    
    /** 
     * Función que crea una url recortada a partir de una url original
     * @param @RequestParam("url")
     * @return ResponseEntity<ShortURL>
     * @throws IOException
     */
    @RequestMapping(value = "/link", method = RequestMethod.POST)
    public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
            @RequestParam(value = "sponsor", required = false) String sponsor, HttpServletRequest request)
            throws IOException {
        UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });

        if (urlValidator.isValid(url) && validatorService.validate(url) == "Constructable") {
            ShortURL su = shortUrlService.save(url, sponsor, request.getRemoteAddr());
            HttpHeaders h = new HttpHeaders();
            h.setLocation(su.getUri());
            return new ResponseEntity<>(su, h, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    
    /** 
     * Función que crea todas las url recortadas a partir de un CSV
     * @param @RequestParam("linklist[]"
     * @return ResponseEntity<List<ShortURL>>
     * @throws IOException
     */
    @MessageMapping("/csv")
    @SendTo("/topic/links")
    public ShortURL SaveCSV(String item) throws IOException {
        ShortURL su = new ShortURL();
        UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });
        if (urlValidator.isValid(item) && validatorService.validate(item) == "Constructable") {
            su = shortUrlService.save(item, null, null);
        }
        return su;
    }

    
    /** 
     * Funcion que devuelve todas las url recortadas
     * @return ResponseEntity<List<ShortURL>>
     * @throws WriterException
     * @throws IOException
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<ShortURL>> listar() throws WriterException, IOException {

        List<ShortURL> aDevolver = shortUrlService.list(100L, 0L);
        // Guarda los click de cada URL
        aDevolver.forEach(item -> {
            item.setClicks(clickService.clicksByHash(item.getHash()));
        });

        return new ResponseEntity<>(aDevolver, HttpStatus.CREATED);
    }

    
    /** 
     * Funcion que devuelve el QR de una url recortada a partir de su hash
     * @param hash
     * @return ResponseEntity<ShortURL>
     * @throws IOException
     * @throws WriterException
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/qr", method = RequestMethod.GET)
    public ResponseEntity<ShortURL> crearQr(@RequestParam("hash") String hash) throws IOException, WriterException, URISyntaxException {

        ShortURL s = new ShortURL();
        s.setQR(qr.getQRCodeImage(ServletUriComponentsBuilder.fromCurrentContextPath().path("/" + hash).build().toUriString(), 150, 150));
        return new ResponseEntity<>(s, HttpStatus.CREATED);
    }


    
    
    /** 
     * @param request
     * @return String
     */
    private String extractIP(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    
    /** 
     * @param l
     * @return ResponseEntity<?>
     */
    private ResponseEntity<?> createSuccessfulRedirectToResponse(ShortURL l) {
        HttpHeaders h = new HttpHeaders();
        h.setLocation(URI.create(l.getTarget()));
        return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
    }
}
