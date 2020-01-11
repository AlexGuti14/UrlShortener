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
    
    
    /** 
     * Funcion que verifica de manera periodica las 10 urls recortadas que no se han verificado recientemente
     * @throws IOException
     */
    @Scheduled(fixedDelay = 50000 ) // La función se ejecutará cada fixedDelay
	public void VerificacionPeriodica() throws IOException {
        List<ShortURL> aDevolver = shortUrlService.listByValidation(10L, 0L);
        if (!aDevolver.isEmpty()){
            for (ShortURL elemento: aDevolver){
                System.out.println(elemento.getHash());
                // Chequea si la url es alcanzable, elimina de la bbdd si no lo es
                if(validatorService.validate(elemento.getTarget()) != "Constructable"){
                    shortUrlService.delete(elemento.getHash());
                }
                else {
                    //Actualiza el timestamp de la url
                    shortUrlService.updateValidation(elemento);
                    validatorService.updateCache(elemento.getHash());
                }
            }
        }
	}
}
