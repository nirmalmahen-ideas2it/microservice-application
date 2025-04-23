package com.sample.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

/**
 * DTO for Role details.
 * Represents role data for fetching the role details, including audit information.
 */
@Getter
@Builder
public class RoleInfo {

    private final Long id;
    private final String name;
    private final Instant createdDate;
    private final String createdBy;
    private final Instant lastModifiedDate;
    private final String lastModifiedBy;

}
