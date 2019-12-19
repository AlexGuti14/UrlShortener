package urlshortener.domain;

import java.net.URI;
import java.sql.Date;
import java.sql.Timestamp;

public class ShortURL {

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
    private Long clicks;
    private byte[] qr;
    private Timestamp validation;

    public ShortURL(String hash, String target, URI uri, String sponsor,
                    Date created, String owner, Integer mode, Boolean safe, String ip,
                    String country, Timestamp validation) {
        this.hash = hash;
        this.target = target;
        this.uri = uri;
        this.sponsor = sponsor;
        this.created = created;
        this.owner = owner;
        this.mode = mode;
        this.safe = safe;
        this.ip = ip;
        this.country = country;
        this.clicks = 0L;
        this.validation = validation;
    }

    public ShortURL() {
    }

    public String getHash() {
        return hash;
    }

    public String getTarget() {
        return target;
    }

    public URI getUri() {
        return uri;
    }

    public Date getCreated() {
        return created;
    }

    public String getOwner() {
        return owner;
    }

    public Integer getMode() {
        return mode;
    }

    public String getSponsor() {
        return sponsor;
    }

    public Boolean getSafe() {
        return safe;
    }

    public String getIP() {
        return ip;
    }

    public String getCountry() {
        return country;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }

    public byte[] getQR() {
        return qr;
    }

    public void setQR(byte[] qr) {
        this.qr = qr;
    }

    public Timestamp getTimestamp(){
        return validation;
    }

    public void setTimestamp(Timestamp val) {
        this.validation = val;
    }
}
