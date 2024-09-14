package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.multilevelcache.CacheManager;

public class CacheApplication {
    private static final Logger log = LogManager.getLogger(CacheApplication.class);

    public static void main(String[] args) {

        CacheManager cacheManager = new CacheManager();
        cacheManager.addCacheLevel("LRU", 10, 1, 2);
        cacheManager.addCacheLevel("LRU", 20, 2, 4);
        cacheManager.addCacheLevel("FIFO", 40, 4, 8);
        cacheManager.addCacheLevel("PersistentLRU", 80, 8, 16);

        cacheManager.write("1", "abc");
        cacheManager.write("2", "bcd");
        cacheManager.write("3", "cde");

        log.info(cacheManager.read("1"));
        log.info(cacheManager.read("5"));

        cacheManager.printStatistics();
    }
}