package com.ideas2it.training.jhipster.userrole.app.service.impl;

import com.ideas2it.training.jhipster.userrole.app.dto.UserInfo;
import com.ideas2it.training.jhipster.userrole.app.service.caching.UserCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UserCacheServiceImpl implements UserCacheService {

    private static final String USER_KEY_PREFIX = "user:";

    private final RedisTemplate<String, Object> redisTemplate;

    public UserCacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveUser(UserInfo user) {
        redisTemplate.opsForValue().set(USER_KEY_PREFIX + user.getId(), user, Duration.ofMinutes(1));
    }

    @Override
    public UserInfo getUser(Long id) {
        return (UserInfo) redisTemplate.opsForValue().get(USER_KEY_PREFIX + id);
    }

    @Override
    public void deleteUser(Long id) {
        redisTemplate.delete(USER_KEY_PREFIX + id);
    }
}
