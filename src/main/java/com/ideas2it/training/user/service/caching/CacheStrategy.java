package com.ideas2it.training.user.service.caching;

import java.time.Duration;

/**
 * Interface defining the contract for different caching strategies.
 * This allows for different caching implementations (Redis, In-Memory, etc.)
 * to be used interchangeably.
 */
public interface CacheStrategy {
    /**
     * Saves a value in the cache with the specified key.
     *
     * @param key      the key to store the value under
     * @param value    the value to store
     * @param duration the time-to-live for the cached value
     */
    void save(String key, Object value, Duration duration);

    /**
     * Retrieves a value from the cache by its key.
     *
     * @param key the key to look up
     * @return the cached value, or null if not found
     */
    Object get(String key);

    /**
     * Deletes a value from the cache by its key.
     *
     * @param key the key to delete
     */
    void delete(String key);
}