package com.sample.service.impl;

import com.sample.dto.UserInfo;
import com.sample.service.caching.UserCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Service implementation for caching user data.
 * <p>
 * This class provides methods to save, retrieve, and delete user data in a Redis cache.
 * </p>
 *
 * @author Alagu Nirmal Mahendran
 * @since 05-06-2025
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {

    private static final String USER_KEY_PREFIX = "user:";

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Constructor to initialize the RedisTemplate.
     *
     * @param redisTemplate the RedisTemplate for interacting with the Redis cache
     */
    public UserCacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Saves a user to the Redis cache with a time-to-live of 1 minute.
     *
     * @param user the UserInfo object to be cached
     */
    @Override
    public void saveUser(UserInfo user) {
        redisTemplate.opsForValue().set(USER_KEY_PREFIX + user.getId(), user, Duration.ofMinutes(1));
    }

    /**
     * Retrieves a user from the Redis cache by ID.
     *
     * @param id the ID of the user to retrieve
     * @return the UserInfo object if found in the cache, otherwise null
     */
    @Override
    public UserInfo getUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return (UserInfo) redisTemplate.opsForValue().get(USER_KEY_PREFIX + id);
    }

    /**
     * Deletes a user from the Redis cache by ID.
     *
     * @param id the ID of the user to delete
     */
    @Override
    public void deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        redisTemplate.delete(USER_KEY_PREFIX + id);
    }
}
