package com.ideas2it.training.user.controller;

import com.ideas2it.training.user.dto.UserInfo;
import com.ideas2it.training.user.service.caching.UserCacheService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class NoOpUserCacheServiceConfig {
    @Bean
    public UserCacheService userCacheService() {
        return new UserCacheService() {
            @Override
            public void saveUser(UserInfo user) {
            }

            @Override
            public UserInfo getUser(Long id) {
                return null;
            }

            @Override
            public void deleteUser(Long id) {
            }
        };
    }
}