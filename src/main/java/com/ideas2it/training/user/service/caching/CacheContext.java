package com.ideas2it.training.user.service.caching;

import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Context class that uses different caching strategies.
 * This implements the Strategy pattern by allowing different caching behaviors
 * to be used interchangeably.
 */
@Component
public class CacheContext {
    private CacheStrategy cacheStrategy;

    public CacheContext(CacheStrategy defaultStrategy) {
        this.cacheStrategy = defaultStrategy;
    }

    /**
     * Sets the current caching strategy.
     *
     * @param strategy the strategy to use
     */
    public void setCacheStrategy(CacheStrategy strategy) {
        this.cacheStrategy = strategy;
    }

    /**
     * Saves a value using the current strategy.
     *
     * @param key      the key to store the value under
     * @param value    the value to store
     * @param duration the time-to-live for the cached value
     */
    public void save(String key, Object value, Duration duration) {
        cacheStrategy.save(key, value, duration);
    }

    /**
     * Retrieves a value using the current strategy.
     *
     * @param key the key to look up
     * @return the cached value, or null if not found
     */
    public Object get(String key) {
        return cacheStrategy.get(key);
    }

    /**
     * Deletes a value using the current strategy.
     *
     * @param key the key to delete
     */
    public void delete(String key) {
        cacheStrategy.delete(key);
    }
}