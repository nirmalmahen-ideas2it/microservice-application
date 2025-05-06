package com.sample.dto;

import com.sample.enums.AuthStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthValidationResponse {
    private AuthStatus status;
    private String message;
}

