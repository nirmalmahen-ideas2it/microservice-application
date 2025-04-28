package com.sample.service;

import com.sample.domain.PagedResponse;
import com.sample.service.dto.UserCreateDto;
import com.sample.service.dto.UserInfo;
import com.sample.service.dto.UserUpdateDto;
import com.sample.dto.UserCreateDto;
import com.sample.dto.UserInfo;
import com.sample.dto.UserUpdateDto;

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
