package com.ideas2it.training.user.service.caching;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryCacheStrategyTest {
    @Test
    void testSaveAndGet() {
        InMemoryCacheStrategy cache = new InMemoryCacheStrategy();
        cache.save("key1", "value1", Duration.ofSeconds(10));
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    void testDelete() {
        InMemoryCacheStrategy cache = new InMemoryCacheStrategy();
        cache.save("key2", "value2", Duration.ofSeconds(10));
        cache.delete("key2");
        assertNull(cache.get("key2"));
    }

    @Test
    void testExpiration() throws InterruptedException {
        InMemoryCacheStrategy cache = new InMemoryCacheStrategy();
        cache.save("key3", "value3", Duration.ofMillis(100));
        Thread.sleep(150);
        assertNull(cache.get("key3"));
    }

    @Test
    void testGetNonExistentKey() {
        InMemoryCacheStrategy cache = new InMemoryCacheStrategy();
        assertNull(cache.get("nope"));
    }
}