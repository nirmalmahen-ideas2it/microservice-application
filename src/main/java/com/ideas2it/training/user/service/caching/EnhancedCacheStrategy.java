package com.ideas2it.training.user.service.caching;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

/**
 * Enhanced interface for cache strategies with additional features.
 * Extends the basic CacheStrategy with eviction policies and statistics.
 */
public interface EnhancedCacheStrategy extends CacheStrategy {
    /**
     * Gets the current eviction policy.
     *
     * @return the current eviction policy
     */
    EvictionPolicy getEvictionPolicy();

    /**
     * Sets the eviction policy.
     *
     * @param policy the eviction policy to use
     */
    void setEvictionPolicy(EvictionPolicy policy);

    /**
     * Gets the maximum size of the cache.
     *
     * @return the maximum number of items the cache can hold
     */
    long getMaxSize();

    /**
     * Sets the maximum size of the cache.
     *
     * @param maxSize the maximum number of items the cache can hold
     */
    void setMaxSize(long maxSize);

    /**
     * Gets the current size of the cache.
     *
     * @return the number of items currently in the cache
     */
    long getCurrentSize();

    /**
     * Gets cache statistics.
     *
     * @return a map containing cache statistics
     */
    Map<String, Long> getStats();

    /**
     * Gets all keys in the cache.
     *
     * @return a set of all keys currently in the cache
     */
    Set<String> getAllKeys();

    /**
     * Clears all items from the cache.
     */
    void clear();

    /**
     * Checks if the cache contains a key.
     *
     * @param key the key to check
     * @return true if the key exists in the cache
     */
    boolean containsKey(String key);

    /**
     * Gets the time-to-live for a key.
     *
     * @param key the key to check
     * @return the remaining time-to-live, or null if the key doesn't exist
     */
    Duration getTimeToLive(String key);
}