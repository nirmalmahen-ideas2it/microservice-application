package com.sample.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;

@Getter
@Builder
public class UserInfo {

    private final Long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String mobile;
    private final String address;
    private final String postalCode;
    private final Set<String> roles;
    private final Instant createdDate;
    private final String createdBy;
    private final Instant lastModifiedDate;
    private final String lastModifiedBy;
}
