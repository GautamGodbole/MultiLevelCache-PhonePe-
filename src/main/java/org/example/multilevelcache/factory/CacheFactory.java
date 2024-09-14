package org.example.multilevelcache.factory;

import org.example.multilevelcache.cache.Cache;
import org.example.multilevelcache.cache.FIFOCache;
import org.example.multilevelcache.cache.LRUCache;
import org.example.multilevelcache.cache.PersistenLRUCache;

/*
 * Class : CacheFactory
 *
 * Provides a new instance of cache based on cacheType
 */
public class CacheFactory {

    public static Cache getCache(String cacheType, int capacity, int readTime, int writeTime) {

        return switch (cacheType) {
            case "LRU" -> new LRUCache(capacity, readTime, writeTime);
            case "FIFO" -> new FIFOCache(capacity, readTime, writeTime);
            case "PersistentLRU" -> new PersistenLRUCache(capacity, readTime, writeTime);
            default -> throw new IllegalArgumentException("CacheType not valid!");
        };

    }
}
