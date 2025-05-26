package com.ideas2it.training.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for Paged Response.
 * Represents paginated response data for API results.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    private List<T> items;
    private long totalElements;
    private int page;
    private int size;
}
