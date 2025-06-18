package com.ideas2it.training.user.service.caching;

/**
 * Enum defining different cache eviction policies.
 */
public enum EvictionPolicy {
    /**
     * Least Recently Used - removes the least recently accessed items first
     */
    LRU,

    /**
     * First In First Out - removes the oldest items first
     */
    FIFO,

    /**
     * Least Frequently Used - removes the least frequently accessed items first
     */
    LFU,

    /**
     * Time To Live - removes items based on their expiration time
     */
    TTL
}