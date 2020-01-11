package urlshortener.service;

import com.google.common.hash.Hashing;
import org.springframework.http.HttpStatus;
import urlshortener.domain.ShortURL;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.function.Function;

public class ShortURLBuilder {

    private String hash;
    private String target;
    private URI uri;
    private String sponsor;
    private Date created;
    private String owner;
    private Integer mode;
    private Boolean safe;
    private String ip;
    private String country;
    private Timestamp validation;

    
    /** 
     * @return ShortURLBuilder
     */
    static ShortURLBuilder newInstance() {
        return new ShortURLBuilder();
    }

    
    /** 
     * @return ShortURL
     */
    ShortURL build() {
        return new ShortURL(
                hash,
                target,
                uri,
                sponsor,
                created,
                owner,
                mode,
                safe,
                ip,
                country,
                validation
        );
    }

    
    /** 
     * @param url
     * @return ShortURLBuilder
     */
    ShortURLBuilder target(String url) {
        target = url;
        hash = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
        return this;
    }

    
    /** 
     * @param sponsor
     * @return ShortURLBuilder
     */
    ShortURLBuilder sponsor(String sponsor) {
        this.sponsor = sponsor;
        return this;
    }

    
    /** 
     * @return ShortURLBuilder
     */
    ShortURLBuilder createdNow() {
        this.created = new Date(System.currentTimeMillis());
        return this;
    }

    
    /** 
     * @return ShortURLBuilder
     */
    ShortURLBuilder randomOwner() {
        this.owner = UUID.randomUUID().toString();
        return this;
    }

    
    /** 
     * @return ShortURLBuilder
     */
    ShortURLBuilder temporaryRedirect() {
        this.mode = HttpStatus.TEMPORARY_REDIRECT.value();
        return this;
    }

    
    /** 
     * @return ShortURLBuilder
     */
    ShortURLBuilder treatAsSafe() {
        this.safe = true;
        return this;
    }

    
    /** 
     * @param ip
     * @return ShortURLBuilder
     */
    ShortURLBuilder ip(String ip) {
        this.ip = ip;
        return this;
    }

    
    /** 
     * @return ShortURLBuilder
     */
    ShortURLBuilder unknownCountry() {
        this.country = null;
        return this;
    }

    
    /** 
     * @param extractor
     * @return ShortURLBuilder
     */
    ShortURLBuilder uri(Function<String, URI> extractor) {
        this.uri = extractor.apply(hash);
        return this;
    }

    
    /** 
     * @return ShortURLBuilder
     */
    ShortURLBuilder validationNow() {
        this.validation = new Timestamp(System.currentTimeMillis());
        return this;
    }
}
