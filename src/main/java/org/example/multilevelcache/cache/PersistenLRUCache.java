package org.example.multilevelcache.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Class : PersistentLRUCache
 *
 * Special implementation of LRU Cache.
 * Keeps the key-value pairs in map.
 * Also, stores the map into file structure (persistent).
 */
public class PersistenLRUCache extends LRUCache{

    private static final Logger log = LogManager.getLogger(PersistenLRUCache.class);
    private final String fileStorageName = "Backups/PersistentLRUCache";
    private File fileStorage;
    ExecutorService executorService;
    ObjectMapper objectMapper;

    public PersistenLRUCache(int capacity, int readTimeInNanos, int writeTimeInNanos) {
        super(capacity, readTimeInNanos, writeTimeInNanos);

        executorService = Executors.newSingleThreadExecutor();
        objectMapper = new ObjectMapper();
        this.fileStorage = new File(fileStorageName);
        this.loadCache();
    }

    private void loadCache() {

        String fcn = "loadCache:";
        if(!fileStorage.exists())
            return;

        try (InputStream inputStream = new FileInputStream(fileStorage)){
            cache = objectMapper.readValue(inputStream, Map.class);
        } catch (IOException | NullPointerException exception) {
            log.error("{} exception : {}", fcn, exception);
        }
    }

    private void writeToCache() {

        String fcn = "writeToCache:";

        executorService.execute(() -> {
            try (OutputStream outputStream = new FileOutputStream(fileStorage)) {
                objectMapper.writeValue(outputStream, cache);
            } catch (IOException exception) {
                log.error("{} exception : {}", fcn, exception);
            }
        });
    }

    @Override
    public String get(String key) {
        return super.get(key);
    }

    @Override
    public void put(String key, String value) {
        super.put(key, value);
        this.writeToCache();
    }
}
