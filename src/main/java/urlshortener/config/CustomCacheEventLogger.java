package urlshortener.config;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCacheEventLogger implements CacheEventListener<Object, Object> {

    private static final Logger LOG= LoggerFactory.getLogger(CustomCacheEventLogger.class);

    
    /** 
     * @param cacheEvent
     */
    @Override
    public void onEvent(CacheEvent<Object, Object> cacheEvent) {
        LOG.info("custom Caching event {} {} {} {} ", cacheEvent.getType(),cacheEvent.getKey(),cacheEvent.getOldValue(),cacheEvent.getNewValue());
    }
}
