package org.example.multilevelcache.cache;

import java.util.Map;

/*
 * Class : Cache
 *
 * Abstract class to provide a structure to all types of caches
 */
public abstract class Cache {

    protected int capacity; // number of keys this cache can store
    protected Map<String, String> cache; // Map to maintain the key-value pairs
    protected int readTimeInNanos, writeTimeInNanos; // read/write time in ns

    public abstract String get(String key);

    public abstract void put(String key, String value);

    public int getUsage() {
        return cache.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public int getReadTime() {
        return readTimeInNanos;
    }

    public int getWriteTime() {
        return writeTimeInNanos;
    }
}
