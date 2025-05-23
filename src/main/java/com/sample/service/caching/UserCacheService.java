package com.sample.service.caching;

import com.sample.dto.UserInfo;

public interface UserCacheService {
    void saveUser(UserInfo user);

    UserInfo getUser(Long id);

    void deleteUser(Long id);
}
