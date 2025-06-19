package com.ideas2it.training.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for submitting a password reset token and new password.
 */
@Getter
@Setter
public class PasswordResetTokenDto {
    @NotBlank
    private String token;
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;
}