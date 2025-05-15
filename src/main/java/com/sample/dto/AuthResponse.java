package com.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object for Authentication Response.
 * Represents the payload for user login responses, including token and refresh token.
 * Used for transferring authentication data between server and client.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@AllArgsConstructor
@Getter
public class AuthResponse {
    private String token;
    private String refreshToken;
}
