package com.ideas2it.training.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for password reset response messages.
 */
@Getter
@Setter
@AllArgsConstructor
public class PasswordResetResponseDto {
    private String message;
}