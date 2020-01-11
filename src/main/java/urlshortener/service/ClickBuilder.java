package urlshortener.service;

import urlshortener.domain.Click;

import java.sql.Date;

public class ClickBuilder {

    private String hash;
    private Date created;
    private String referrer;
    private String browser;
    private String platform;
    private String ip;
    private String country;

    
    /** 
     * @return ClickBuilder
     */
    static ClickBuilder newInstance() {
        return new ClickBuilder();
    }

    
    /** 
     * @return Click
     */
    Click build() {
        return new Click(null, hash, created, referrer,
                browser, platform, ip, country);
    }

    
    /** 
     * @param hash
     * @return ClickBuilder
     */
    ClickBuilder hash(String hash) {
        this.hash = hash;
        return this;
    }

    
    /** 
     * @return ClickBuilder
     */
    ClickBuilder createdNow() {
        this.created = new Date(System.currentTimeMillis());
        return this;
    }

    
    /** 
     * @return ClickBuilder
     */
    ClickBuilder noReferrer() {
        this.referrer = null;
        return this;
    }

    
    /** 
     * @return ClickBuilder
     */
    ClickBuilder unknownBrowser() {
        this.browser = null;
        return this;
    }

    
    /** 
     * @return ClickBuilder
     */
    ClickBuilder unknownPlatform() {
        this.platform = null;
        return this;
    }


    
    /** 
     * @param ip
     * @return ClickBuilder
     */
    ClickBuilder ip(String ip) {
        this.ip = ip;
        return this;
    }

    
    /** 
     * @return ClickBuilder
     */
    ClickBuilder withoutCountry() {
        this.country = null;
        return this;
    }

}
