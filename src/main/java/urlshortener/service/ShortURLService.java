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

    public ShortURL findByKey(String id) {
        return shortURLRepository.findByKey(id);
    }

    // FUncion para listar
    public List<ShortURL> list(Long longitud, Long offset) {
        System.out.println("Ejecucion listar de ShortURLService");
        return shortURLRepository.list(longitud, offset);
    }

    //Funcion para borrar un elemento
    public void delete(String hash) {
        System.out.println("Ejecucion Borrar de ShortURLService");
        shortURLRepository.delete(hash);
    }

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
                .build();
        return shortURLRepository.save(su);
    }
}
