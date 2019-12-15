package urlshortener.web;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import urlshortener.domain.ShortURL;

import urlshortener.service.ShortURLService;
import urlshortener.service.ValidatorService;

import java.io.IOException;
import java.util.*;


@RestController
public class ValidatorController {
    private final ShortURLService shortUrlService;
    private final ValidatorService validatorService;

    public ValidatorController(ShortURLService shortUrlService, ValidatorService validatorService) {
        this.shortUrlService = shortUrlService;
        this.validatorService = validatorService;
    }
    
    // TODO: verificar solo unas pocas que no se hayan verificado recientemente.
    /* 
     * Funcion que verifica de manera periodica diferentes urls recortadas
     */ 
    @Scheduled(fixedDelay = 50000 ) // Function will be executed X time after the last one finishes
	public void VerificacionPeriodica() throws IOException {
        List<ShortURL> aDevolver = shortUrlService.list(100L, 0L);
        if (!aDevolver.isEmpty()){
            for (ShortURL elemento: aDevolver){
                // Check if the URI is reachable, delete from database if not. If it is reachable the ehcache is updated.
                if(validatorService.validate(elemento.getTarget()) != "Constructable"){
                    shortUrlService.delete(elemento.getHash());
                }
                else {
                    validatorService.updateCache(elemento.getHash());
                }
            }
        }
	}
}
