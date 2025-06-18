package com.ideas2it.training.user.service.caching;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Enhanced Redis implementation of the cache strategy.
 * Includes eviction policies and statistics tracking.
 */
@Component
public class EnhancedRedisCacheStrategy implements EnhancedCacheStrategy {

    private final RedisTemplate<String, Object> redisTemplate;
    private EvictionPolicy evictionPolicy = EvictionPolicy.TTL;
    private long maxSize = 1000;
    private final Map<String, Long> accessCount = new ConcurrentHashMap<>();
    private final Map<String, Long> lastAccessTime = new ConcurrentHashMap<>();
    private final AtomicLong hitCount = new AtomicLong(0);
    private final AtomicLong missCount = new AtomicLong(0);

    public EnhancedRedisCacheStrategy(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String key, Object value, Duration duration) {
        if (getCurrentSize() >= maxSize) {
            evict();
        }
        redisTemplate.opsForValue().set(key, value, duration);
        accessCount.put(key, 0L);
        lastAccessTime.put(key, System.currentTimeMillis());
    }

    @Override
    public Object get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            hitCount.incrementAndGet();
            accessCount.merge(key, 1L, Long::sum);
            lastAccessTime.put(key, System.currentTimeMillis());
        } else {
            missCount.incrementAndGet();
        }
        return value;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
        accessCount.remove(key);
        lastAccessTime.remove(key);
    }

    @Override
    public EvictionPolicy getEvictionPolicy() {
        return evictionPolicy;
    }

    @Override
    public void setEvictionPolicy(EvictionPolicy policy) {
        this.evictionPolicy = policy;
    }

    @Override
    public long getMaxSize() {
        return maxSize;
    }

    @Override
    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public long getCurrentSize() {
        return redisTemplate.keys("*").size();
    }

    @Override
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("hits", hitCount.get());
        stats.put("misses", missCount.get());
        stats.put("size", getCurrentSize());
        stats.put("maxSize", maxSize);
        return stats;
    }

    @Override
    public Set<String> getAllKeys() {
        return redisTemplate.keys("*");
    }

    @Override
    public void clear() {
        redisTemplate.delete(getAllKeys());
        accessCount.clear();
        lastAccessTime.clear();
        hitCount.set(0);
        missCount.set(0);
    }

    @Override
    public boolean containsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Duration getTimeToLive(String key) {
        Long ttl = redisTemplate.getExpire(key);
        return ttl != null && ttl > 0 ? Duration.ofSeconds(ttl) : null;
    }

    private void evict() {
        switch (evictionPolicy) {
            case LRU -> evictLRU();
            case LFU -> evictLFU();
            case FIFO -> evictFIFO();
            case TTL -> evictTTL();
        }
    }

    private void evictLRU() {
        String oldestKey = lastAccessTime.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        if (oldestKey != null) {
            delete(oldestKey);
        }
    }

    private void evictLFU() {
        String leastFrequentKey = accessCount.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        if (leastFrequentKey != null) {
            delete(leastFrequentKey);
        }
    }

    private void evictFIFO() {
        String oldestKey = lastAccessTime.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        if (oldestKey != null) {
            delete(oldestKey);
        }
    }

    private void evictTTL() {
        String expiringKey = getAllKeys().stream()
                .filter(key -> getTimeToLive(key) != null)
                .min(Comparator.comparing(key -> getTimeToLive(key).toSeconds()))
                .orElse(null);
        if (expiringKey != null) {
            delete(expiringKey);
        }
    }
}