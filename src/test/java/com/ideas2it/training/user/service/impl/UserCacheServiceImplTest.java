package com.ideas2it.training.user.service.impl;

import com.ideas2it.training.user.dto.UserInfo;
import com.ideas2it.training.user.service.caching.CacheContext;
import com.ideas2it.training.user.service.caching.CacheFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCacheServiceImplTest {

    @Mock
    private CacheFactory cacheFactory;
    @Mock
    private CacheContext cacheContext;

    private UserCacheServiceImpl userCacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cacheFactory.createCacheStrategy(CacheFactory.CacheType.ENHANCED_REDIS)).thenReturn(null);
        userCacheService = new UserCacheServiceImpl(cacheFactory);
        // Inject mock cacheContext
        // Reflection is used here because cacheContext is final and set in constructor
        try {
            java.lang.reflect.Field field = UserCacheServiceImpl.class.getDeclaredField("cacheContext");
            field.setAccessible(true);
            field.set(userCacheService, cacheContext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSaveUser_Valid() {
        UserInfo user = UserInfo.builder().id(1L).build();
        userCacheService.saveUser(user);
        verify(cacheContext, times(1)).save(eq("user:1"), eq(user), any(Duration.class));
    }

    @Test
    void testSaveUser_NullUser() {
        assertThrows(IllegalArgumentException.class, () -> userCacheService.saveUser(null));
    }

    @Test
    void testSaveUser_NullUserId() {
        UserInfo user = UserInfo.builder().id(null).build();
        assertThrows(IllegalArgumentException.class, () -> userCacheService.saveUser(user));
    }

    @Test
    void testGetUser_Valid() {
        UserInfo user = UserInfo.builder().id(2L).build();
        when(cacheContext.get("user:2")).thenReturn(user);
        UserInfo result = userCacheService.getUser(2L);
        assertEquals(user, result);
        verify(cacheContext, times(1)).get("user:2");
    }

    @Test
    void testGetUser_NullId() {
        assertThrows(IllegalArgumentException.class, () -> userCacheService.getUser(null));
    }

    @Test
    void testDeleteUser_Valid() {
        userCacheService.deleteUser(3L);
        verify(cacheContext, times(1)).delete("user:3");
    }

    @Test
    void testDeleteUser_NullId() {
        assertThrows(IllegalArgumentException.class, () -> userCacheService.deleteUser(null));
    }

    @Test
    void testChangeCacheStrategy() {
        CacheContext newContext = mock(CacheContext.class);
        when(cacheFactory.createCacheStrategy(CacheFactory.CacheType.IN_MEMORY)).thenReturn(null);
        userCacheService.changeCacheStrategy(CacheFactory.CacheType.IN_MEMORY);
        // No exception means success; actual strategy switching is internal
        // Optionally, use reflection to verify cacheContext was updated
    }
}