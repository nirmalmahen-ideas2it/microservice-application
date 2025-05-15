package com.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object for Authentication Request.
 * Represents the payload for user login requests, including username and password.
 * Used for transferring authentication data between client and server.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@Getter
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
}
