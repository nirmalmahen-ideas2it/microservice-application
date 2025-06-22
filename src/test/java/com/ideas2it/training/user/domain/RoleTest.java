package com.ideas2it.training.user.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {
    @Test
    void testRolePojo() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        assertEquals(1L, role.getId());
        assertEquals("ADMIN", role.getName());
        assertNotNull(role.toString());
        assertEquals(role, role);
        assertNotEquals(role, null);
        assertNotEquals(role, new Object());
        assertEquals(role.hashCode(), role.hashCode());
    }
}