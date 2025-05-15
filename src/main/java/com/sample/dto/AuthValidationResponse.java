package com.sample.dto;

import com.sample.enums.AuthStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for Authentication Validation Response.
 * Represents the payload for authentication validation responses, including status and message.
 * Used for transferring validation results between server and client.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@Data
@AllArgsConstructor
public class AuthValidationResponse {
    private AuthStatus status;
    private String message;
}

