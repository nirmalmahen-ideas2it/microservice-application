package com.ideas2it.training.user.service.caching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnhancedRedisCacheStrategyTest {
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;
    private EnhancedRedisCacheStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.keys("*")).thenReturn(new HashSet<>());
        strategy = new EnhancedRedisCacheStrategy(redisTemplate);
    }

    @Test
    void testSaveAndGet() {
        when(redisTemplate.keys("*")).thenReturn(Collections.emptySet());
        strategy.save("key", "val", Duration.ofSeconds(10));
        verify(valueOperations).set("key", "val", Duration.ofSeconds(10));
    }

    @Test
    void testGet() {
        when(valueOperations.get("key")).thenReturn("val");
        Object result = strategy.get("key");
        assertEquals("val", result);
    }

    @Test
    void testDelete() {
        strategy.delete("key");
        verify(redisTemplate).delete("key");
    }

    @Test
    void testEvictionPolicy() {
        strategy.setEvictionPolicy(EvictionPolicy.LFU);
        assertEquals(EvictionPolicy.LFU, strategy.getEvictionPolicy());
    }

    @Test
    void testMaxSize() {
        strategy.setMaxSize(10);
        assertEquals(10, strategy.getMaxSize());
    }

    @Test
    void testGetCurrentSize() {
        when(redisTemplate.keys("*")).thenReturn(Set.of("a", "b"));
        assertEquals(2, strategy.getCurrentSize());
    }

    @Test
    void testGetStats() {
        when(redisTemplate.keys("*")).thenReturn(Set.of("a"));
        Map<String, Long> stats = strategy.getStats();
        assertTrue(stats.containsKey("hits"));
        assertTrue(stats.containsKey("misses"));
        assertTrue(stats.containsKey("size"));
        assertTrue(stats.containsKey("maxSize"));
    }

    @Test
    void testGetAllKeys() {
        when(redisTemplate.keys("*")).thenReturn(Set.of("a", "b"));
        assertEquals(Set.of("a", "b"), strategy.getAllKeys());
    }

    @Test
    void testClear() {
        when(redisTemplate.keys("*")).thenReturn(Set.of("a", "b"));
        strategy.clear();
        verify(redisTemplate).delete(Set.of("a", "b"));
    }

    @Test
    void testContainsKey() {
        when(redisTemplate.hasKey("key")).thenReturn(true);
        assertTrue(strategy.containsKey("key"));
    }

    @Test
    void testGetTimeToLive() {
        when(redisTemplate.getExpire("key")).thenReturn(10L);
        assertEquals(Duration.ofSeconds(10), strategy.getTimeToLive("key"));
        when(redisTemplate.getExpire("key")).thenReturn(-1L);
        assertNull(strategy.getTimeToLive("key"));
    }
}