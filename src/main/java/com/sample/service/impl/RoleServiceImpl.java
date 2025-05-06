package com.sample.service.impl;

import com.sample.domain.PagedResponse;
import com.sample.domain.Role;
import com.sample.dto.RoleCreateDto;
import com.sample.dto.RoleInfo;
import com.sample.dto.RoleUpdateDto;
import com.sample.repository.RoleRepository;
import com.sample.service.RoleService;
import com.sample.service.mapper.RoleMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing roles.
 * Handles persistence logic and DTO conversion via MapStruct.
 * <p>
 * This class provides methods to create, update, retrieve, and delete roles.
 * </p>
 *
 * @author Alagu Nirmal Mahendran
 * @since 05-06-2025
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    /**
     * Constructor to initialize RoleRepository and RoleMapper.
     *
     * @param roleRepository the repository for role persistence
     * @param roleMapper     the mapper for converting between Role entities and DTOs
     */
    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    /**
     * Creates a new role.
     *
     * @param dto the RoleCreateDto containing role creation details
     * @return the created RoleInfo
     */
    @Override
    public RoleInfo create(@Valid RoleCreateDto dto) {
        Role role = roleMapper.toEntity(dto);
        return roleMapper.toInfo(roleRepository.save(role));
    }

    /**
     * Updates an existing role.
     *
     * @param dto the RoleUpdateDto containing role update details
     * @return the updated RoleInfo
     */
    @Override
    public RoleInfo update(RoleUpdateDto dto) {
        Role role = roleRepository.findById(dto.getId()).orElseThrow();
        role.setName(dto.getName().name());
        return roleMapper.toInfo(roleRepository.save(role));
    }

    /**
     * Partially updates an existing role.
     * Only updates non-null fields.
     *
     * @param dto the RoleUpdateDto containing partial role update details
     * @return the partially updated RoleInfo
     */
    @Override
    public RoleInfo partialUpdate(RoleUpdateDto dto) {
        Role role = roleRepository.findById(dto.getId()).orElseThrow();
        if (dto.getName() != null) role.setName(dto.getName().name());
        return roleMapper.toInfo(roleRepository.save(role));
    }

    /**
     * Retrieves a role by ID.
     *
     * @param id the ID of the role to retrieve
     * @return an Optional containing the RoleInfo if found
     */
    @Override
    public Optional<RoleInfo> getById(Long id) {
        return roleRepository.findById(id).map(roleMapper::toInfo);
    }

    /**
     * Retrieves all roles.
     *
     * @return a list of all RoleInfo objects
     */
    @Override
    public List<RoleInfo> getAll() {
        List<Role> roles = roleRepository.findAll();
        List<RoleInfo> resultRolesList = roles.stream()
            .map(roleMapper::toInfo)
            .toList();
        return resultRolesList;
    }

    /**
     * Retrieves a paginated list of roles.
     *
     * @param offset the starting index of the page
     * @param limit  the number of roles per page
     * @return a PagedResponse containing the paginated RoleInfo objects
     */
    public PagedResponse<RoleInfo> getAllPaged(int offset, int limit) {
        PageRequest pageRequest = PageRequest.of(offset / limit, limit);
        Page<Role> page = roleRepository.findAll(pageRequest);

        List<RoleInfo> roleInfos = page.getContent().stream()
            .map(roleMapper::toInfo)
            .toList();

        return new PagedResponse<>(
            roleInfos,
            page.getTotalElements(),
            page.getNumber(),
            page.getSize()
        );
    }

    /**
     * Deletes a role by ID.
     *
     * @param id the ID of the role to delete
     */
    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}
