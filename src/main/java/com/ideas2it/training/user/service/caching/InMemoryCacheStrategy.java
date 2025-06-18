package com.ideas2it.training.user.service.caching;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the CacheStrategy interface.
 * Uses a ConcurrentHashMap for thread-safe caching operations.
 */
@Component
public class InMemoryCacheStrategy implements CacheStrategy {
    private final Map<String, CacheEntry> cache;

    public InMemoryCacheStrategy() {
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public void save(String key, Object value, Duration duration) {
        cache.put(key, new CacheEntry(value, System.currentTimeMillis() + duration.toMillis()));
    }

    @Override
    public Object get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return entry.getValue();
    }

    @Override
    public void delete(String key) {
        cache.remove(key);
    }

    /**
     * Inner class to store cache entries with expiration time.
     */
    private static class CacheEntry {
        private final Object value;
        private final long expirationTime;

        public CacheEntry(Object value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        public Object getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}