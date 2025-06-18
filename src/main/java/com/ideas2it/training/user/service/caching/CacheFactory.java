package com.ideas2it.training.user.service.caching;

import org.springframework.stereotype.Component;

/**
 * Factory class for creating cache strategies.
 * This implements the Factory pattern to create different types of cache
 * implementations.
 */
@Component
public class CacheFactory {

 
    private final InMemoryCacheStrategy inMemoryCacheStrategy;
    private final EnhancedRedisCacheStrategy enhancedRedisCacheStrategy;

    public CacheFactory(
          
            InMemoryCacheStrategy inMemoryCacheStrategy,
            EnhancedRedisCacheStrategy enhancedRedisCacheStrategy) {
        
        this.inMemoryCacheStrategy = inMemoryCacheStrategy;
        this.enhancedRedisCacheStrategy = enhancedRedisCacheStrategy;
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
           
            case IN_MEMORY -> inMemoryCacheStrategy;
            case ENHANCED_REDIS -> enhancedRedisCacheStrategy;
            default -> throw new IllegalArgumentException("Unsupported cache type: " + type);
        };
    }

    /**
     * Creates an enhanced cache strategy based on the specified type.
     *
     * @param type the type of enhanced cache strategy to create
     * @return the created EnhancedCacheStrategy instance
     * @throws IllegalArgumentException if the cache type is not supported
     */
    public EnhancedCacheStrategy createEnhancedCacheStrategy(CacheType type) {
        return switch (type) {
            case ENHANCED_REDIS -> enhancedRedisCacheStrategy;
            default -> throw new IllegalArgumentException("Unsupported enhanced cache type: " + type);
        };
    }

    /**
     * Enum defining the supported cache types.
     */
    public enum CacheType {
        
        IN_MEMORY,
        ENHANCED_REDIS
    }
}