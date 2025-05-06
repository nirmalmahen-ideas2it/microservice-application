package com.sample.web.rest.controller;

import com.sample.dto.AuthRequest;
import com.sample.dto.AuthValidationResponse;
import com.sample.service.auth.AuthValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling authentication validation requests.
 * <p>
 * This controller provides an endpoint to validate user authentication requests.
 * It interacts with the {@link AuthValidationService} to process the validation logic.
 * </p>
 *
 * @author Alagu Nirmal Mahendran
 * @since 05-06-2025
 */
@RestController
@RequestMapping("/api/auth")
public class AuthValidationController {

    private final AuthValidationService authService;

    public AuthValidationController(AuthValidationService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Validate authentication request", description = "Validates the provided authentication request and returns the validation result.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validation successful",
                    content = @Content(schema = @Schema(implementation = AuthValidationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid authentication request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/validate")
    public AuthValidationResponse validate(@RequestBody AuthRequest request) {
        return authService.validateUser(request);
    }
}
