package com.sample.service;

import com.sample.domain.PagedResponse;
import com.sample.dto.RoleCreateDto;
import com.sample.dto.RoleInfo;
import com.sample.dto.RoleUpdateDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing roles.
 * Defines business operations for Role entity.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-22
 */
public interface RoleService {

    RoleInfo create(@Valid RoleCreateDto roleDto);

    RoleInfo update(RoleUpdateDto roleDto);

    RoleInfo partialUpdate(RoleUpdateDto roleDto);

    Optional<RoleInfo> getById(Long id);

    List<RoleInfo> getAll();

    PagedResponse<RoleInfo> getAllPaged(int offset, int limit);

    void delete(Long id);
}
