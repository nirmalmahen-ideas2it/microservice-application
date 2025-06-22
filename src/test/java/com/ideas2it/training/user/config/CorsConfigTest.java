package com.ideas2it.training.user.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {
    @Test
    void testCorsConfigurerBean() {
        CorsConfig config = new CorsConfig();
        WebMvcConfigurer webMvcConfigurer = config.corsConfigurer();
        assertNotNull(webMvcConfigurer);
        CorsRegistry registry = new CorsRegistry();
        // Should not throw
        webMvcConfigurer.addCorsMappings(registry);
    }
}