package org.example.multilevelcache.statistics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/*
 * Class : CacheStatistics
 *
 * Record keeper of cache operations
 */
public class CacheStatistics {

    private static final Logger log = LogManager.getLogger(CacheStatistics.class);
    private final int CACHE_READ_WRITE_STATISTICS_PERIOD = 5;
    private Queue<Integer> readStatistics;
    private Queue<Integer> writeStatistics;
    private ArrayList<CacheParameters> cacheStatus;

    public CacheStatistics() {
        readStatistics = new LinkedList<>();
        writeStatistics = new LinkedList<>();
        cacheStatus = new ArrayList<>();
    }

    // Records the read time of last READ
    public void recordReadTime(Integer time) {

        String fcn = "recordReadTime:";

        if (readStatistics.size() >= CACHE_READ_WRITE_STATISTICS_PERIOD)
            readStatistics.remove();

        readStatistics.add(time);
        log.debug("{} in(ns) : {}", fcn, time);
    }

    // Records the write time of last WRITE
    public void recordWriteTime(Integer time) {

        String fcn = "recordWriteTime:";

        if (writeStatistics.size() >= CACHE_READ_WRITE_STATISTICS_PERIOD)
            writeStatistics.remove();

        writeStatistics.add(time);
        log.debug("{} in(ns) : {}", fcn, time);
    }

    // Add a new cache level to internally maintained data
    public void addCacheLevel(int capacity) {
        cacheStatus.add(new CacheParameters(capacity));
    }

    // Updates cache usage at a particular level
    public void updateCacheUsage(int level, int usage) {
        cacheStatus.get(level).setUsage(usage);
    }

    // Updates cache hits at a particular level
    public void updateCacheHit(int level) {
        cacheStatus.get(level).incrementHit();
    }

    // Updates cache miss(s) at a particular level
    public void updateCacheMiss(int level) {
        cacheStatus.get(level).incrementMiss();
    }

    // Prints the detailed summary of all caches
    public void printSummary() {

        int i;

        log.info("----- Cache Summary ------");
        log.info("Average Read Time - last 5 READs (in ns) : {}", (int) readStatistics.stream().mapToInt(Integer::intValue).average().orElse(0));
        log.info("Average Write Time - last 5 WRITEs (in ns) : {}", (int) writeStatistics.stream().mapToInt(Integer::intValue).average().orElse(0));

        log.info("---- Cache Statistics -----");
        for (i = 0; i < cacheStatus.size(); i++) {
            log.info("Cache{} : {}", (i+1), cacheStatus.get(i).toString());
        }
    }
}
