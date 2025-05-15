package com.sample.dto;

import com.sample.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * Data Transfer Object for User Update.
 * Represents user data for updating an existing user, including personal details and roles.
 * Used for transferring user update data between client and server.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@Getter
@Builder
public class UserUpdateDto {

    @NotNull
    private final Long id;
    private final String username;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String mobile;
    private final String address;
    private final String postalCode;
    private final Set<RoleType> roles;

}
