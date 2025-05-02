package com.ideas2it.training.jhipster.userrole.app.dto;

import com.ideas2it.training.jhipster.userrole.app.enums.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class UserCreateDto {

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
