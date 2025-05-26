package com.ideas2it.training.user.service.caching;

import com.ideas2it.training.user.dto.UserInfo;

public interface UserCacheService {
    void saveUser(UserInfo user);

    UserInfo getUser(Long id);

    void deleteUser(Long id);
}
