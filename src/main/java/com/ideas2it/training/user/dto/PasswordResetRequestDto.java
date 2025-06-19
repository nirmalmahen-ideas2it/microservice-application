package com.ideas2it.training.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for requesting a password reset.
 */
@Getter
@Setter
public class PasswordResetRequestDto {
    @NotBlank
    private String emailOrUsername;
}