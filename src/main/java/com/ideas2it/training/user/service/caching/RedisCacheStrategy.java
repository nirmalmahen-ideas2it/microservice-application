package com.ideas2it.training.user.service.caching;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Redis implementation of the CacheStrategy interface.
 * Uses RedisTemplate for caching operations.
 */
@Component
public class RedisCacheStrategy implements CacheStrategy {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheStrategy(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String key, Object value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}