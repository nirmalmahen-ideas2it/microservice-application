package com.sample.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import static org.junit.jupiter.api.Assertions.*;

class RedisCacheConfigTest {

    private RedisCacheConfig redisCacheConfig;

    @Mock
    private RedisConnectionFactory mockConnectionFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        redisCacheConfig = new RedisCacheConfig();
    }

    @Test
    void testRedisConnectionFactory() {
        // Arrange
        redisCacheConfig.redisHost = "localhost";
        redisCacheConfig.redisPort = 6379;

        // Act
        RedisConnectionFactory factory = redisCacheConfig.redisConnectionFactory();

        // Assert
        assertNotNull(factory);
        assertTrue(factory instanceof LettuceConnectionFactory);
        RedisStandaloneConfiguration config = ((LettuceConnectionFactory) factory).getStandaloneConfiguration();
        assertEquals("localhost", config.getHostName());
        assertEquals(6379, config.getPort());
    }

}