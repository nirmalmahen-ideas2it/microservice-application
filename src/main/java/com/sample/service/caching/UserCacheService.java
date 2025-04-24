package com.sample.service.caching;

import com.sample.service.dto.UserInfo;

import java.util.List;

public interface UserCacheService {
    void saveUser(UserInfo user);

    UserInfo getUser(Long id);

    List<UserInfo> getAllUsers();

    void deleteUser(Long id);
}
