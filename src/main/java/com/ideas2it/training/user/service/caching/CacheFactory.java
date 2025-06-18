package com.ideas2it.training.user.service.caching;

import org.springframework.stereotype.Component;

/**
 * Factory class for creating cache strategies.
 * This implements the Factory pattern to create different types of cache
 * implementations.
 */
@Component
public class CacheFactory {

    private final RedisCacheStrategy redisCacheStrategy;

    public CacheFactory(RedisCacheStrategy redisCacheStrategy) {
        this.redisCacheStrategy = redisCacheStrategy;
    }

    /**
     * Creates a cache strategy based on the specified type.
     *
     * @param type the type of cache strategy to create
     * @return the created CacheStrategy instance
     * @throws IllegalArgumentException if the cache type is not supported
     */
    public CacheStrategy createCacheStrategy(CacheType type) {
        return switch (type) {
            case REDIS -> redisCacheStrategy;
            // Add more cases for other cache types as needed
            default -> throw new IllegalArgumentException("Unsupported cache type: " + type);
        };
    }

    /**
     * Enum defining the supported cache types.
     */
    public enum CacheType {
        REDIS
        // Add more cache types as needed
    }
}