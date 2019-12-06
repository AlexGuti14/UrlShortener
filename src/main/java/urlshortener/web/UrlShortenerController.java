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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class UrlShortenerController {
    private final ShortURLService shortUrlService;
    private final ValidatorService validatorService;
    private final GenerarQRService qr = new GenerarQRService();
    private final ClickService clickService;

    public UrlShortenerController(ShortURLService shortUrlService, ClickService clickService, ValidatorService validatorService) {
        this.shortUrlService = shortUrlService;
        this.clickService = clickService;
        this.validatorService = validatorService;

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

        if (urlValidator.isValid(url) && validatorService.validate(url) == "Constructable") {
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
            if (urlValidator.isValid(linklist[i]) && validatorService.validate(linklist[i]) == "Constructable") {
                su = shortUrlService.save(linklist[i], sponsor, request.getRemoteAddr());
            }
        }
    }

    // Funcion Listar
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<ShortURL>> listar(HttpServletRequest request) throws WriterException, IOException {

        List<ShortURL> aDevolver = shortUrlService.list(100L, 0L);

        // Guarda los click de cada URL
        aDevolver.forEach(item -> {
            item.setClicks(clickService.clicksByHash(item.getHash()));
        });

        return new ResponseEntity<>(aDevolver, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/qr", method = RequestMethod.GET)
    public ResponseEntity<ShortURL> crearQr(@RequestParam("hash") String hash, HttpServletRequest request)
            throws IOException, WriterException, URISyntaxException {
        System.out.println("Hash para cread el qr: " + hash);
        ShortURL s = new ShortURL();
        s.setQR(qr.getQRCodeImage("http://" + obtenerIP() + ":8080/" + hash, 150, 150));
        System.out.println(obtenerIP());
        return new ResponseEntity<>(s, HttpStatus.CREATED);
    }

    /*
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
                if(!validatorService.validate(elemento.getTarget())){
                    shortUrlService.delete(elemento.getHash());
                }
            }
        }
	}*/

    private String extractIP(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    String obtenerIP() throws UnknownHostException {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    private ResponseEntity<?> createSuccessfulRedirectToResponse(ShortURL l) {
        HttpHeaders h = new HttpHeaders();
        h.setLocation(URI.create(l.getTarget()));
        return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
    }
}
