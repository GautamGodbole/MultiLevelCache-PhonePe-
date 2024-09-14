package org.example.multilevelcache.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * Class : LRUCache
 *
 * LRU (Least Recently Used) implementation of Cache
 */
public class LRUCache extends Cache{

    private static final Logger log = LogManager.getLogger(LRUCache.class);

    public LRUCache(int capacity, int readTimeInNanos, int writeTimeInNanos) {
        this.capacity = capacity;
        cache = new LinkedHashMap<>(capacity, 0.75f, true) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > capacity;
            }
        };

        this.readTimeInNanos = readTimeInNanos;
        this.writeTimeInNanos = writeTimeInNanos;
    }

    @Override
    public String get(String key) {
        return cache.get(key);
    }

    @Override
    public void put(String key, String value) {
        cache.put(key, value);
    }

}
