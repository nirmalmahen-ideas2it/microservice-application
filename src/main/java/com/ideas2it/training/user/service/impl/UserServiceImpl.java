package com.ideas2it.training.user.service.impl;

import com.ideas2it.training.user.dto.PagedResponse;
import com.ideas2it.training.user.domain.Role;
import com.ideas2it.training.user.domain.User;
import com.ideas2it.training.user.dto.UserCreateDto;
import com.ideas2it.training.user.dto.UserInfo;
import com.ideas2it.training.user.dto.UserUpdateDto;
import com.ideas2it.training.user.enums.RoleType;
import com.ideas2it.training.user.repository.RoleRepository;
import com.ideas2it.training.user.repository.UserRepository;
import com.ideas2it.training.user.service.UserService;
import com.ideas2it.training.user.service.caching.UserCacheService;
import com.ideas2it.training.user.service.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for managing users.
 * Handles persistence logic and DTO conversion via MapStruct.
 *
 * @author Alagu Nirmal Mahendran
 * @since 05-06-2025
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserCacheService userCacheService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, UserCacheService userCacheService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userCacheService = userCacheService;
    }

    /**
     * Creates a new user.
     * Encodes the password, resolves roles, and saves the user to the database.
     * Also caches the created user.
     *
     * @param dto the UserCreateDto containing user creation details
     * @return the created UserInfo
     */
    @Override
    public UserInfo create(UserCreateDto dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(resolveRoles(dto.getRoles()));
        User savedUser = userRepository.save(user);
        UserInfo userInfo = userMapper.toInfo(savedUser);
        userCacheService.saveUser(userInfo);
        return userInfo;
    }

    /**
     * Updates an existing user.
     * Updates all fields and roles, then saves the user to the database.
     * Also updates the cached user.
     *
     * @param dto the UserUpdateDto containing user update details
     * @return the updated UserInfo
     */
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
        User updatedUser = userRepository.save(user);
        UserInfo userInfo = userMapper.toInfo(updatedUser);
        userCacheService.saveUser(userInfo);
        return userInfo;
    }

    /**
     * Partially updates an existing user.
     * Only updates non-null fields and roles, then saves the user to the database.
     * Also updates the cached user.
     *
     * @param dto the UserUpdateDto containing partial user update details
     * @return the partially updated UserInfo
     */
    @Override
    public UserInfo partialUpdate(UserUpdateDto dto) {
        User user = userRepository.findById(dto.getId()).orElseThrow();
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getMobile() != null) user.setMobile(dto.getMobile());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        if (dto.getPostalCode() != null) user.setPostalCode(dto.getPostalCode());
        if (dto.getPassword() != null) user.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getRoles() != null) user.setRoles(resolveRoles(dto.getRoles()));
        User updatedUser = userRepository.save(user);
        UserInfo userInfo = userMapper.toInfo(updatedUser);
        userCacheService.saveUser(userInfo);
        return userInfo;
    }

    /**
     * Retrieves a user by ID.
     * First checks the cache, then the database if not found in the cache.
     *
     * @param id the ID of the user to retrieve
     * @return an Optional containing the UserInfo if found
     */
    @Override
    public Optional<UserInfo> getById(Long id) {
        UserInfo cached = userCacheService.getUser(id);
        if (cached != null) return Optional.of(cached);

        Optional<User> user = userRepository.findById(id);
        user.ifPresent(u -> userCacheService.saveUser(userMapper.toInfo(u)));
        return user.map(userMapper::toInfo);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all UserInfo objects
     */
    @Override
    public List<UserInfo> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toInfo)
                .toList();
    }

    /**
     * Retrieves a paginated list of users.
     *
     * @param offset the starting index of the page
     * @param limit  the number of users per page
     * @return a PagedResponse containing the paginated UserInfo objects
     */
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

    /**
     * Deletes a user by ID.
     * Removes the user from the database and the cache.
     *
     * @param id the ID of the user to delete
     */
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        userCacheService.deleteUser(id);
    }

    /**
     * Resolves roles from a set of RoleType enums.
     * Fetches Role entities from the database based on the provided RoleType names.
     *
     * @param roleNames the set of RoleType enums
     * @return a set of resolved Role entities
     */
    private Set<Role> resolveRoles(Set<RoleType> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) return new HashSet<>();
        return roleNames.stream()
                .map(roleName -> {
                    RoleType roleType = RoleType.valueOf(roleName.name().toUpperCase());
                    return roleRepository.findByName(roleType.name())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName));
                })
                .collect(Collectors.toSet());
    }
}
