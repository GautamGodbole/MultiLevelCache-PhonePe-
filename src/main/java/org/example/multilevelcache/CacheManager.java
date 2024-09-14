package org.example.multilevelcache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.multilevelcache.cache.Cache;
import org.example.multilevelcache.factory.CacheFactory;
import org.example.multilevelcache.statistics.CacheStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * Class : CacheManager
 *
 * Manages multiple levels of caches (L1, L2, ..., Ln)
 * Provides interface for external application to use MultiLevelCache as a library
 */
public class CacheManager {

    private static final Logger log = LogManager.getLogger(CacheManager.class);
    List<Cache> caches;
    private int totalCaches;

    private CacheStatistics cacheStatistics;
    private ExecutorService executorService;
    private Future<?> future;

    public CacheManager() {
        this.caches = new ArrayList<>();
        this.totalCaches = 0;
        this.cacheStatistics = new CacheStatistics();
        this.executorService = Executors.newSingleThreadExecutor();
        this.future = null;
    }

    // Add Cache to the list of Caches
    public void addCacheLevel(String cacheType, int capacity, int readTimeInNanos, int writeTimeInNanos) {
        String fcn = "addCacheLevel:";
        Cache cache = CacheFactory.getCache(cacheType, capacity, readTimeInNanos, writeTimeInNanos);
        caches.add(cache);

        cacheStatistics.addCacheLevel(cache.getCapacity());
        cacheStatistics.updateCacheUsage(totalCaches, cache.getUsage());

        totalCaches++;
        log.debug("{} Added Cache of type : {}, capacity = {}, readTime(ns) = {}, writeTime(ns) = {}", fcn, cacheType, cacheType, readTimeInNanos, writeTimeInNanos);
    }

    // Chained read through list of Caches
    public String read(String key) {

        String fcn = "read:";
        int i, readTime = 0;
        String value = null;

        try {
            if (future != null) {
                future.get();
                future = null;
            }
        } catch (ExecutionException | InterruptedException exception) {
            log.error("{} exception {}", fcn, exception);
        }

        for (i = 0; i < totalCaches; i++) {
            value = caches.get(i).get(key);
            readTime += caches.get(i).getReadTime();
            if (value != null) {
                cacheStatistics.updateCacheHit(i);
                break;
            }
            else
                cacheStatistics.updateCacheMiss(i);
        }
        if (i < totalCaches) {
            log.info("{} found key : {} with value {} at level : {}", fcn, key, value, i);
            i--;
            for (; i >= 0; i--) {
                caches.get(i).put(key, value);
                readTime += caches.get(i).getWriteTime();
                cacheStatistics.updateCacheUsage(i, caches.get(i).getUsage());
            }
        }
        else
            log.info("{} key : {} not found", fcn, key);

        cacheStatistics.recordReadTime(readTime);
        return value;
    }

    // Chained asynchronous write to list of caches
    public void write(String key, String value) {

        String fcn = "write:";
        future = executorService.submit(() -> {
            int i, writeTime = 0;
            String valueInCache = null;

            for (i = 0; i < totalCaches; i++) {
                valueInCache = caches.get(i).get(key);
                writeTime += caches.get(i).getReadTime();
                if ((valueInCache == null) || (!valueInCache.equals(value))) {
                    caches.get(i).put(key, value);
                    writeTime += caches.get(i).getWriteTime();
                    cacheStatistics.updateCacheUsage(i, caches.get(i).getUsage());
                }
                else
                    break;
            }

            cacheStatistics.recordWriteTime(writeTime);
            log.info("{} wrote key : {} in time(ns) : {}", fcn, key, writeTime);
        });
    }

    // Prints the statistics of caches
    public void printStatistics() {
        cacheStatistics.printSummary();
    }
}
