package com.sample.service.impl;

import com.sample.domain.PagedResponse;
import com.sample.domain.Role;
import com.sample.domain.User;
import com.sample.enums.RoleType;
import com.sample.repository.RoleRepository;
import com.sample.repository.UserRepository;
import com.sample.service.UserService;
import com.sample.service.dto.UserCreateDto;
import com.sample.service.dto.UserInfo;
import com.sample.service.dto.UserUpdateDto;
import com.sample.service.mapper.UserMapper;
import com.sample.util.PasswordUtil;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for managing users.
 * Handles persistence logic and DTO conversion via MapStruct.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserInfo create(UserCreateDto dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setRoles(resolveRoles(dto.getRoles()));
        return userMapper.toInfo(userRepository.save(user));
    }

    @Override
    public UserInfo update(UserUpdateDto dto) {
        User user = userRepository.findById(dto.getId()).orElseThrow();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setAddress(dto.getAddress());
        user.setPostalCode(dto.getPostalCode());
        user.setRoles(resolveRoles(dto.getRoles()));
        return userMapper.toInfo(userRepository.save(user));
    }

    @Override
    public UserInfo partialUpdate(UserUpdateDto dto) {
        User user = userRepository.findById(dto.getId()).orElseThrow();
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getMobile() != null) user.setMobile(dto.getMobile());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        if (dto.getPostalCode() != null) user.setPostalCode(dto.getPostalCode());
        if (dto.getPassword() != null) user.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
        if (dto.getRoles() != null) user.setRoles(resolveRoles(dto.getRoles()));
        return userMapper.toInfo(userRepository.save(user));
    }

    @Override
    public Optional<UserInfo> getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toInfo);
    }

    @Override
    public List<UserInfo> getAll() {
        List<User> users = userRepository.findAll();
        List<UserInfo> resultUserList = users.stream()
            .map(userMapper::toInfo)
            .toList();
        return resultUserList;
    }

    @Override
    public PagedResponse<UserInfo> getAllPaged(int offset, int limit) {
        PageRequest pageRequest = PageRequest.of(offset / limit, limit);
        Page<User> page = userRepository.findAll(pageRequest);

        List<UserInfo> userInfos = page.getContent().stream()
            .map(userMapper::toInfo)
            .toList();

        return new PagedResponse<>(
            userInfos,
            page.getTotalElements(),
            page.getNumber(),
            page.getSize()
        );
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private Set<Role> resolveRoles(Set<RoleType> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) return new HashSet<>();

        // Convert role names to RoleType enum and fetch corresponding Role entities
        return roleNames.stream()
            .map(roleName -> {
                RoleType roleType = RoleType.valueOf(roleName.name().toUpperCase()); // Convert to enum
                return roleRepository.findByName(roleType.name()) // Fetch Role entity by name
                    .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName));
            })
            .collect(Collectors.toSet());
    }
}
