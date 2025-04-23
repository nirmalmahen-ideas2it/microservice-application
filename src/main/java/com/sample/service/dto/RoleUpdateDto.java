package com.sample.service.dto;

import com.sample.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * Data Transfer Object for Role.
 * Represents role data for creating or updating a role.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@Getter
@Builder
public class RoleUpdateDto {

    @NotNull
    private final Long id;

    @Schema(description = "Role type", example = "USER", allowableValues = {"ADMIN", "USER", "MAINTAINER"})
    private final RoleType name; // Use RoleType enum

}
