package com.sample.service.impl;

import com.sample.domain.PagedResponse;
import com.sample.domain.Role;
import com.sample.repository.RoleRepository;
import com.sample.service.RoleService;
import com.sample.dto.RoleCreateDto;
import com.sample.dto.RoleInfo;
import com.sample.dto.RoleUpdateDto;
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
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-22
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleInfo create(@Valid RoleCreateDto dto) {
        Role role = roleMapper.toEntity(dto);
        return roleMapper.toInfo(roleRepository.save(role));
    }

    @Override
    public RoleInfo update(RoleUpdateDto dto) {
        Role role = roleRepository.findById(dto.getId()).orElseThrow();
        role.setName(dto.getName().name());
        return roleMapper.toInfo(roleRepository.save(role));
    }

    @Override
    public RoleInfo partialUpdate(RoleUpdateDto dto) {
        Role role = roleRepository.findById(dto.getId()).orElseThrow();
        if (dto.getName() != null) role.setName(dto.getName().name());
        return roleMapper.toInfo(roleRepository.save(role));
    }

    @Override
    public Optional<RoleInfo> getById(Long id) {
        return roleRepository.findById(id).map(roleMapper::toInfo);
    }

    @Override
    public List<RoleInfo> getAll() {
        List<Role> roles = roleRepository.findAll();
        List<RoleInfo> resultRolesList = roles.stream()
            .map(roleMapper::toInfo)
            .toList();
        return resultRolesList;
    }

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


    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

}
