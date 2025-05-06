package com.sample.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuration for Redis caching.
 * <p>
 * This class sets up the Redis connection factory and template for caching
 * with custom serialization and deserialization.
 * </p>
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-06-05
 */
@Configuration
public class RedisCacheConfig {

    @Value("${spring.cache.redis.host}")
    private String redisHost;

    @Value("${spring.cache.redis.port}")
    private int redisPort;

    @Value("${spring.cache.redis.password}")
    private String redisPassword;

    /**
     * Configures the Redis connection factory.
     * <p>
     * This method sets up a standalone Redis configuration with host, port,
     * and password details.
     * </p>
     *
     * @return the RedisConnectionFactory bean
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(RedisPassword.of(redisPassword));
        return new LettuceConnectionFactory(config);
    }

    /**
     * Configures the Redis template.
     * <p>
     * This method sets up a Redis template with custom key and value serializers,
     * including support for Java time module and polymorphic type handling.
     * </p>
     *
     * @param factory the RedisConnectionFactory
     * @return the RedisTemplate bean
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());

        // Register Java Time Module
        Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        jsonSerializer.setObjectMapper(mapper);
        template.setValueSerializer(jsonSerializer);
        return template;
    }
}
