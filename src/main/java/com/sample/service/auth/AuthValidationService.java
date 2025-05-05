package com.sample.service.auth;

import com.sample.dto.AuthRequest;
import com.sample.dto.AuthValidationResponse;
import com.sample.enums.AuthStatus;
import com.sample.service.impl.CustomUserDetailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthValidationService {

    private final CustomUserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public AuthValidationService(CustomUserDetailService userDetailService, PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthValidationResponse validateUser(AuthRequest request) {
        try {
            UserDetails userDetails = userDetailService.loadUserByUsername(request.getUsername());
            boolean validUser= passwordEncoder.matches(request.getPassword(), userDetails.getPassword());
            if (validUser) {
                return new AuthValidationResponse(AuthStatus.SUCCESS, "Valid credentials");
            } else {
                return new AuthValidationResponse(AuthStatus.FAILURE, "Invalid credentials");
            }
        } catch (UsernameNotFoundException e) {
            return new AuthValidationResponse(AuthStatus.FAILURE, "Exception occurred during User validation: "+ e.getMessage());
        }
    }
}
