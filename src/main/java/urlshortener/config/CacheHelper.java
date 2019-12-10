package urlshortener.config;

import java.util.concurrent.TimeUnit;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;


public class CacheHelper {

    private CacheManager cacheManager;
    private Cache<String, byte[]> qrsCache;

    public CacheHelper() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        cacheManager.init();
        // Falta pero solo es aplicable al tipo cacheConfiguration --> setMemoryStoreEvictionPolicy("LRU");

        // CONTINUAR MIRANDO https://www.baeldung.com/ehcache
        qrsCache = cacheManager.createCache("qrsCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, byte[].class,
                        ResourcePoolsBuilder.heap(10)).withExpiry(
                                Expirations.timeToLiveExpiration(Duration.of(3600, TimeUnit.SECONDS))).build());
        
    }
 
    public Cache<String, byte[]> getqrsCacheFromCacheManager() {
        return cacheManager.getCache("qrsCache", String.class, byte[].class);
    }
}