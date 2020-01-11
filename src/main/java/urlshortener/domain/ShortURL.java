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

    
    /** 
     * @return String
     */
    public String getHash() {
        return hash;
    }

    
    /** 
     * @return String
     */
    public String getTarget() {
        return target;
    }

    
    /** 
     * @return URI
     */
    public URI getUri() {
        return uri;
    }

    
    /** 
     * @return Date
     */
    public Date getCreated() {
        return created;
    }

    
    /** 
     * @return String
     */
    public String getOwner() {
        return owner;
    }

    
    /** 
     * @return Integer
     */
    public Integer getMode() {
        return mode;
    }

    
    /** 
     * @return String
     */
    public String getSponsor() {
        return sponsor;
    }

    
    /** 
     * @return Boolean
     */
    public Boolean getSafe() {
        return safe;
    }

    
    /** 
     * @return String
     */
    public String getIP() {
        return ip;
    }

    
    /** 
     * @return String
     */
    public String getCountry() {
        return country;
    }

    
    /** 
     * @return Long
     */
    public Long getClicks() {
        return clicks;
    }

    
    /** 
     * @param clicks
     */
    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }

    
    /** 
     * @return byte[]
     */
    public byte[] getQR() {
        return qr;
    }

    
    /** 
     * @param qr
     */
    public void setQR(byte[] qr) {
        this.qr = qr;
    }

    
    /** 
     * @return Timestamp
     */
    public Timestamp getTimestamp(){
        return validation;
    }

    
    /** 
     * @param val
     */
    public void setTimestamp(Timestamp val) {
        this.validation = val;
    }
}
