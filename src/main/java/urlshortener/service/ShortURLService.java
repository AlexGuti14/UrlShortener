package urlshortener.service;

import org.springframework.stereotype.Service;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ShortURLRepository;
import urlshortener.web.UrlShortenerController;

import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class ShortURLService {

    private final ShortURLRepository shortURLRepository;

    public ShortURLService(ShortURLRepository shortURLRepository) {
        this.shortURLRepository = shortURLRepository;
    }

    
    /** 
     * @param id
     * @return ShortURL
     */
    public ShortURL findByKey(String id) {
        return shortURLRepository.findByKey(id);
    }

    
    /** 
     * @param longitud
     * @param offset
     * @return List<ShortURL>
     */
    // Funcion para listar
    public List<ShortURL> list(Long longitud, Long offset) {
        System.out.println("Ejecucion listar de ShortURLService");
        return shortURLRepository.list(longitud, offset);
    }

    
    /** 
     * @param longitud
     * @param offset
     * @return List<ShortURL>
     */
    // FUncion para listar por orden de validacion antigua
    public List<ShortURL> listByValidation(Long longitud, Long offset) {
        System.out.println("Ejecucion listar por validacion de ShortURLService");
        return shortURLRepository.listByValidation(longitud, offset);
    }

    
    /** 
     * @param hash
     */
    //Funcion para borrar un elemento
    public void delete(String hash) {
        System.out.println("Ejecucion Borrar de ShortURLService");
        shortURLRepository.delete(hash);
    }

    
    /** 
     * @param url
     * @param sponsor
     * @param ip
     * @return ShortURL
     */
    public ShortURL save(String url, String sponsor, String ip) {
        ShortURL su = ShortURLBuilder.newInstance()
                .target(url)
                .uri((String hash) -> linkTo(methodOn(UrlShortenerController.class).redirectTo(hash, null)).toUri())
                .sponsor(sponsor)
                .createdNow()
                .randomOwner()
                .temporaryRedirect()
                .treatAsSafe()
                .ip(ip)
                .unknownCountry()
                .validationNow()
                .build();
        return shortURLRepository.save(su);
    }

    
    /** 
     * Funcion para borrar un elemento
     * @param su
     */
    public void updateValidation(ShortURL su) {
        System.out.println("Ejecucion actualizar de ShortURLService");
        shortURLRepository.updateValidation(su);;
    }
}
