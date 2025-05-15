package com.sample.service.impl;

import com.sample.domain.Role;
import com.sample.domain.User;
import com.sample.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    void testLoadUserByUsername_ValidUser() {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("validUser");
        mockUser.setPassword("encodedPassword");
        mockUser.setRoles(Set.of(new Role() {{
            setName("USER");
        }}));

        when(userRepository.findByUsername("validUser")).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = customUserDetailService.loadUserByUsername("validUser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("validUser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        verify(userRepository, times(1)).findByUsername("validUser");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailService.loadUserByUsername("nonExistentUser"));
        assertEquals("User not found with username: nonExistentUser", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("nonExistentUser");
    }

    @Test
    void testLoadUserByUsername_NullUsername() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                customUserDetailService.loadUserByUsername(null));
        verifyNoInteractions(userRepository);
    }

    @Test
    void testLoadUserByUsername_EmptyUsername() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                customUserDetailService.loadUserByUsername(""));
        verifyNoInteractions(userRepository);
    }
}