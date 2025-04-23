package com.sample.service;

import com.sample.domain.PagedResponse;
import com.sample.service.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    PagedResponse <RoleInfo> getAllPaged(int offset, int limit);

    void delete(Long id);
}
