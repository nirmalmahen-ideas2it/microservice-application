package com.ideas2it.training.user.service.impl;

import com.ideas2it.training.user.dto.UserInfo;
import com.ideas2it.training.user.service.caching.CacheFactory;
import com.ideas2it.training.user.service.caching.CacheStrategy;
import com.ideas2it.training.user.service.caching.UserCacheService;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Service implementation for caching user data.
 * Uses the CacheStrategy pattern through the CacheFactory.
 * <p>
 * This class provides methods to save, retrieve, and delete user data in a
 * Redis cache.
 * </p>
 *
 * @author Alagu Nirmal Mahendran
 * @since 05-06-2025
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {

    private static final String USER_KEY_PREFIX = "user:";
    private static final Duration CACHE_DURATION = Duration.ofMinutes(1);

    private final CacheFactory cacheFactory;
    private final CacheStrategy cacheStrategy;

    /**
     * Constructor to initialize the CacheFactory and CacheStrategy.
     *
     * @param cacheFactory the CacheFactory for creating CacheStrategy instances
     */
    public UserCacheServiceImpl(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
        this.cacheStrategy = cacheFactory.createCacheStrategy(CacheFactory.CacheType.REDIS);
    }

    /**
     * Saves a user to the Redis cache with a time-to-live of 1 minute.
     *
     * @param user the UserInfo object to be cached
     */
    @Override
    public void saveUser(UserInfo user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User or user ID cannot be null");
        }
        cacheStrategy.save(USER_KEY_PREFIX + user.getId(), user, CACHE_DURATION);
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
        return (UserInfo) cacheStrategy.get(USER_KEY_PREFIX + id);
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
        cacheStrategy.delete(USER_KEY_PREFIX + id);
    }
}
