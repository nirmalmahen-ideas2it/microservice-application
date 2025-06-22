package com.ideas2it.training.user.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testUserPojo() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("pass");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("email@test.com");
        user.setMobile("1234567890");
        user.setAddress("Address");
        user.setPostalCode("12345");
        user.setResetToken("token");
        user.setResetTokenExpiry(Instant.now());
        user.setRoles(Set.of());
        user.setRowVersion(1L);
        assertEquals("testuser", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertEquals("First", user.getFirstName());
        assertEquals("Last", user.getLastName());
        assertEquals("email@test.com", user.getEmail());
        assertEquals("1234567890", user.getMobile());
        assertEquals("Address", user.getAddress());
        assertEquals("12345", user.getPostalCode());
        assertEquals("token", user.getResetToken());
        assertNotNull(user.getResetTokenExpiry());
        assertNotNull(user.getRoles());
        assertEquals(1L, user.getRowVersion());
        assertNotNull(user.toString());
        assertEquals(user, user);
        assertNotEquals(user, null);
        assertNotEquals(user, new Object());
        assertEquals(user.hashCode(), user.hashCode());
    }
}