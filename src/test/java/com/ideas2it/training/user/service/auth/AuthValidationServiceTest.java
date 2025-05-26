package com.ideas2it.training.user.service.auth;

import com.ideas2it.training.user.dto.AuthRequest;
import com.ideas2it.training.user.dto.AuthValidationResponse;
import com.ideas2it.training.user.enums.AuthStatus;
import com.ideas2it.training.user.service.impl.CustomUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthValidationServiceTest {

    @Mock
    private CustomUserDetailService userDetailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthValidationService authValidationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateUser_ValidCredentials() {
        // Arrange
        AuthRequest request = new AuthRequest("validUser", "validPassword");
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailService.loadUserByUsername("validUser")).thenReturn(userDetails);
        when(userDetails.getPassword()).thenReturn("encodedPassword");
        when(passwordEncoder.matches("validPassword", "encodedPassword")).thenReturn(true);

        // Act
        AuthValidationResponse response = authValidationService.validateUser(request);

        // Assert
        assertEquals(AuthStatus.SUCCESS, response.getStatus());
        assertEquals("Valid credentials", response.getMessage());
        verify(userDetailService, times(1)).loadUserByUsername("validUser");
        verify(passwordEncoder, times(1)).matches("validPassword", "encodedPassword");
    }

    @Test
    void testValidateUser_InvalidPassword() {
        // Arrange
        AuthRequest request = new AuthRequest("validUser", "invalidPassword");
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailService.loadUserByUsername("validUser")).thenReturn(userDetails);
        when(userDetails.getPassword()).thenReturn("encodedPassword");
        when(passwordEncoder.matches("invalidPassword", "encodedPassword")).thenReturn(false);

        // Act
        AuthValidationResponse response = authValidationService.validateUser(request);

        // Assert
        assertEquals(AuthStatus.FAILURE, response.getStatus());
        assertEquals("Invalid credentials", response.getMessage());
        verify(userDetailService, times(1)).loadUserByUsername("validUser");
        verify(passwordEncoder, times(1)).matches("invalidPassword", "encodedPassword");
    }

    @Test
    void testValidateUser_UserNotFound() {
        // Arrange
        AuthRequest request = new AuthRequest("nonExistentUser", "password");

        when(userDetailService.loadUserByUsername("nonExistentUser")).thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        AuthValidationResponse response = authValidationService.validateUser(request);

        // Assert
        assertEquals(AuthStatus.FAILURE, response.getStatus());
        assertEquals("Exception occurred during User validation: User not found", response.getMessage());
        verify(userDetailService, times(1)).loadUserByUsername("nonExistentUser");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void testValidateUser_NullUsername() {
        // Arrange
        AuthRequest request = new AuthRequest(null, "password");

        when(userDetailService.loadUserByUsername(null)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        AuthValidationResponse response = authValidationService.validateUser(request);

        // Assert
        assertEquals(AuthStatus.FAILURE, response.getStatus());
        assertEquals("Exception occurred during User validation: User not found", response.getMessage());
        verify(userDetailService, times(1)).loadUserByUsername(null);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void testValidateUser_NullPassword() {
        // Arrange
        AuthRequest request = new AuthRequest("validUser", null);
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailService.loadUserByUsername("validUser")).thenReturn(userDetails);
        when(userDetails.getPassword()).thenReturn("encodedPassword");
        when(passwordEncoder.matches(null, "encodedPassword")).thenReturn(false);

        // Act
        AuthValidationResponse response = authValidationService.validateUser(request);

        // Assert
        assertEquals(AuthStatus.FAILURE, response.getStatus());
        assertEquals("Invalid credentials", response.getMessage());
        verify(userDetailService, times(1)).loadUserByUsername("validUser");
        verify(passwordEncoder, times(1)).matches(null, "encodedPassword");
    }
}