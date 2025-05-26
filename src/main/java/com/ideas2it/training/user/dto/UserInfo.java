package com.ideas2it.training.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;

/**
 * Data Transfer Object for User Information.
 * Represents user data for fetching user details, including roles and audit information.
 * Used for transferring user information between server and client.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@Getter
@Builder(toBuilder = true)
@JsonDeserialize(builder = UserInfo.UserInfoBuilder.class)
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

    @JsonPOJOBuilder(withPrefix = "") // Tell Jackson builder methods have no prefix (e.g., "id()" not "withId()")
    public static class UserInfoBuilder {
    }
}
