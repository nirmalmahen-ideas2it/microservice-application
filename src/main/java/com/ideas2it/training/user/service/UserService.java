package com.ideas2it.training.user.service;

import com.ideas2it.training.user.dto.PagedResponse;
import com.ideas2it.training.user.dto.UserCreateDto;
import com.ideas2it.training.user.dto.UserInfo;
import com.ideas2it.training.user.dto.UserUpdateDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing users.
 * Defines business operations for User entity.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
public interface UserService {

    UserInfo create(UserCreateDto userDto);

    UserInfo update(UserUpdateDto userDto);

    UserInfo partialUpdate(UserUpdateDto userDto);

    Optional<UserInfo> getById(Long id);

    List<UserInfo> getAll();

    PagedResponse<UserInfo> getAllPaged(int offset, int limit);

    void delete(Long id);

}
