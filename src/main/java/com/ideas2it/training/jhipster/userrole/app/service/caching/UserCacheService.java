package com.ideas2it.training.jhipster.userrole.app.service.caching;

import com.ideas2it.training.jhipster.userrole.app.dto.UserInfo;

public interface UserCacheService {
    void saveUser(UserInfo user);

    UserInfo getUser(Long id);

    void deleteUser(Long id);
}
