package com.ideas2it.training.user.service.caching;

import com.ideas2it.training.user.dto.UserInfo;

/**
 * Service interface for caching user data.
 * Uses the CacheStrategy pattern for flexible caching implementations.
 */
public interface UserCacheService {
    /**
     * Saves a user to the cache.
     *
     * @param user the UserInfo object to cache
     */
    void saveUser(UserInfo user);

    /**
     * Retrieves a user from the cache by ID.
     *
     * @param id the ID of the user to retrieve
     * @return the cached UserInfo object, or null if not found
     */
    UserInfo getUser(Long id);

    /**
     * Deletes a user from the cache by ID.
     *
     * @param id the ID of the user to delete
     */
    void deleteUser(Long id);
}
