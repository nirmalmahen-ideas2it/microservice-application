package com.ideas2it.training.user.service.auth;

import com.ideas2it.training.user.dto.AuthRequest;
import com.ideas2it.training.user.dto.AuthValidationResponse;
import com.ideas2it.training.user.enums.AuthStatus;
import com.ideas2it.training.user.service.impl.CustomUserDetailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for validating user authentication requests.
 * <p>
 * This class provides methods to validate user credentials
 * using Spring Security's `UserDetailsService` and password encoding.
 * </p>
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-06-05
 */
@Service
public class AuthValidationService {

    private final CustomUserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor to initialize dependencies.
     *
     * @param userDetailService the service for loading user details
     * @param passwordEncoder   the encoder for password validation
     */
    public AuthValidationService(CustomUserDetailService userDetailService, PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Validates user credentials.
     * <p>
     * This method checks if the provided username and password match
     * the stored credentials in the system.
     * </p>
     *
     * @param request the authentication request containing username and password
     * @return an AuthValidationResponse indicating success or failure
     */
    public AuthValidationResponse validateUser(AuthRequest request) {
        try {
            UserDetails userDetails = userDetailService.loadUserByUsername(request.getUsername());
            boolean validUser = passwordEncoder.matches(request.getPassword(), userDetails.getPassword());
            if (validUser) {
                return new AuthValidationResponse(AuthStatus.SUCCESS, "Valid credentials");
            } else {
                return new AuthValidationResponse(AuthStatus.FAILURE, "Invalid credentials");
            }
        } catch (UsernameNotFoundException e) {
            return new AuthValidationResponse(AuthStatus.FAILURE, "Exception occurred during User validation: " + e.getMessage());
        }
    }
}
