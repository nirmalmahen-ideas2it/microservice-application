package com.sample.service.impl;

import com.sample.dto.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCacheServiceImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private UserCacheServiceImpl userCacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testSaveUser_ValidUser() {
        // Arrange
        UserInfo user = UserInfo.builder()
                .id(1L)
                .username("john_doe")
                .firstName("John Doe")
                .build();
        // Act
        userCacheService.saveUser(user);

        // Assert
        verify(valueOperations, times(1))
                .set("user:1", user, Duration.ofMinutes(1));
    }

    @Test
    void testSaveUser_NullUser() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userCacheService.saveUser(null));
        verifyNoInteractions(valueOperations);
    }

    @Test
    void testGetUser_ValidId() {
        // Arrange
        UserInfo user = UserInfo.builder()
                .id(1L)
                .username("john_doe")
                .firstName("John Doe")
                .build();
        when(valueOperations.get("user:1")).thenReturn(user);

        // Act
        UserInfo result = userCacheService.getUser(1L);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
        verify(valueOperations, times(1)).get("user:1");
    }

    @Test
    void testGetUser_InvalidId() {
        // Arrange
        when(valueOperations.get("user:999")).thenReturn(null);

        // Act
        UserInfo result = userCacheService.getUser(999L);

        // Assert
        assertNull(result);
        verify(valueOperations, times(1)).get("user:999");
    }

    @Test
    void testGetUser_NullId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userCacheService.getUser(null));
        verifyNoInteractions(valueOperations);
    }

    @Test
    void testDeleteUser_ValidId() {
        // Act
        userCacheService.deleteUser(1L);

        // Assert
        verify(redisTemplate, times(1)).delete("user:1");
    }

    @Test
    void testDeleteUser_NullId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userCacheService.deleteUser(null));
        verifyNoInteractions(redisTemplate);
    }
}