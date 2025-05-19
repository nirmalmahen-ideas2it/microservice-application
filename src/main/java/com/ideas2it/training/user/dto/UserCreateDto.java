package com.ideas2it.training.user.dto;

import com.ideas2it.training.user.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * Data Transfer Object for User Creation.
 * Represents user data for creating a new user, including personal details and roles.
 * Used for transferring user creation data between client and server.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@Getter
@Builder
public class UserCreateDto {

    @NotNull
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
