package com.sample.web.rest.controller;

import com.sample.dto.AuthRequest;
import com.sample.dto.AuthValidationResponse;
import com.sample.service.auth.AuthValidationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthValidationController {

    private final AuthValidationService authService;

    public AuthValidationController(AuthValidationService authService) {
        this.authService = authService;
    }

    @PostMapping("/validate")
    public AuthValidationResponse validate(@RequestBody AuthRequest request) {
        return authService.validateUser(request);
    }
}
