package com.ideas2it.training.user.service.auth;

import com.ideas2it.training.user.dto.AuthRequest;
import com.ideas2it.training.user.dto.AuthValidationResponse;
import org.springframework.stereotype.Service;

/**
 * Service for validating user authentication requests.
 * Uses the Strategy pattern to support different authentication methods.
 */
@Service
public class AuthValidationService {

    private final AuthContext authContext;

    /**
     * Constructor to initialize the authentication context.
     *
     * @param authContext the context managing authentication strategies
     */
    public AuthValidationService(AuthContext authContext) {
        this.authContext = authContext;
    }

    /**
     * Validates user credentials using the current authentication strategy.
     *
     * @param request the authentication request containing credentials
     * @return an AuthValidationResponse indicating success or failure
     */
    public AuthValidationResponse validateUser(AuthRequest request) {
        return authContext.validate(request);
    }

    /**
     * Changes the authentication strategy.
     *
     * @param strategyType the type of strategy to use
     */
    public void setAuthStrategy(AuthStrategyType strategyType) {
        authContext.setStrategy(strategyType);
    }
}
