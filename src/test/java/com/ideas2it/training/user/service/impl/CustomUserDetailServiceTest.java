package com.ideas2it.training.user.service.impl;

import com.ideas2it.training.user.domain.Role;
import com.ideas2it.training.user.domain.User;
import com.ideas2it.training.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_Valid() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        Role role = new Role();
        role.setName("ADMIN");
        user.setRoles(Set.of(role));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        UserDetails details = customUserDetailService.loadUserByUsername("testuser");
        assertEquals("testuser", details.getUsername());
        assertEquals("password", details.getPassword());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByUsername("notfound")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailService.loadUserByUsername("notfound"));
    }

    @Test
    void testLoadUserByUsername_NullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> customUserDetailService.loadUserByUsername(null));
        assertThrows(IllegalArgumentException.class, () -> customUserDetailService.loadUserByUsername(" "));
    }
}