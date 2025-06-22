package com.ideas2it.training.user.service.caching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CacheFactoryTest {
    private InMemoryCacheStrategy inMemoryCacheStrategy;
    private EnhancedRedisCacheStrategy enhancedRedisCacheStrategy;
    private CacheFactory cacheFactory;

    @BeforeEach
    void setUp() {
        inMemoryCacheStrategy = mock(InMemoryCacheStrategy.class);
        enhancedRedisCacheStrategy = mock(EnhancedRedisCacheStrategy.class);
        cacheFactory = new CacheFactory(inMemoryCacheStrategy, enhancedRedisCacheStrategy);
    }

    @Test
    void testCreateCacheStrategy_InMemory() {
        CacheStrategy strategy = cacheFactory.createCacheStrategy(CacheFactory.CacheType.IN_MEMORY);
        assertEquals(inMemoryCacheStrategy, strategy);
    }

    @Test
    void testCreateCacheStrategy_EnhancedRedis() {
        CacheStrategy strategy = cacheFactory.createCacheStrategy(CacheFactory.CacheType.ENHANCED_REDIS);
        assertEquals(enhancedRedisCacheStrategy, strategy);
    }

    @Test
    void testCreateCacheStrategy_Unsupported() {
        assertThrows(NullPointerException.class, () -> cacheFactory.createCacheStrategy(null));
    }

    @Test
    void testCreateEnhancedCacheStrategy_EnhancedRedis() {
        EnhancedCacheStrategy strategy = cacheFactory
                .createEnhancedCacheStrategy(CacheFactory.CacheType.ENHANCED_REDIS);
        assertEquals(enhancedRedisCacheStrategy, strategy);
    }

    @Test
    void testCreateEnhancedCacheStrategy_Unsupported() {
        assertThrows(IllegalArgumentException.class,
                () -> cacheFactory.createEnhancedCacheStrategy(CacheFactory.CacheType.IN_MEMORY));
    }
}