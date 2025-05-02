package com.ideas2it.training.jhipster.userrole.app.dto;

import com.ideas2it.training.jhipster.userrole.app.enums.RoleType;
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
public class RoleCreateDto {

    @NotNull
    @Schema(description = "Role type", example = "ADMIN", allowableValues = {"ADMIN", "USER", "MAINTAINER"})
    private final RoleType name; // Use RoleType enum

}
